package org.klozevitz.services.implementations.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.DepartmentTelegramView;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.enitites.appUsers.Employee;
import org.klozevitz.enitites.appUsers.enums.states.DepartmentState;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.services.uitl.Registrar;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.inject.Inject;

import static org.klozevitz.enitites.appUsers.enums.states.EmployeeState.BASIC_STATE;
import static org.klozevitz.enitites.appUsers.enums.views.DepartmentView.EMPLOYEES_MANAGEMENT_VIEW;

@Log4j
@RequiredArgsConstructor
public class EmployeeRegistrar implements Registrar {
    private final DepartmentTelegramView telegramView;
    @Inject
    private AppUserRepo appUserRepo;

    @Override
    public SendMessage register(Update update, AppUser currentAppUser) {
        var employeeTgId = update.getMessage().getText();
        var isTgIdValid = isTgIdValid(employeeTgId);

        if (isTgIdValid) {
            var success = registerEmployee(currentAppUser, Long.parseLong(employeeTgId));

            return success ?
                    employeeRegistrationNotificationView(update, currentAppUser) :
                    alreadyRegisteredTgIdErrorView(update, currentAppUser);
        }

        return invalidEmployeeTgIdErrorView(update);
    }

    private boolean registerEmployee(AppUser currentAppUser, long tgId) {
        var persistentAppDepartment = currentAppUser.getDepartment();
        var transientEmployeeAppUser = AppUser.builder()
                .telegramUserId(tgId)
                .employee(
                        Employee.builder()
                                .department(persistentAppDepartment)
                                .state(BASIC_STATE)
                                .build()
                )
                .build();

        try {
            var persistentEmployeeAppUser = appUserRepo.save(transientEmployeeAppUser);

            persistentAppDepartment.getEmployees().add(persistentEmployeeAppUser.getEmployee());
            currentAppUser.getDepartment().setCurrentView(EMPLOYEES_MANAGEMENT_VIEW);

            appUserRepo.save(persistentEmployeeAppUser);
            appUserRepo.save(currentAppUser);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private SendMessage employeeRegistrationNotificationView(Update update, AppUser currentAppUser) {
        currentAppUser.getDepartment().setCurrentView(EMPLOYEES_MANAGEMENT_VIEW);
        currentAppUser.getDepartment().setState(DepartmentState.BASIC_STATE);
        appUserRepo.save(currentAppUser);

        return telegramView.newEmployeeRegistrationNotificationView(update, currentAppUser);
    }

    private SendMessage alreadyRegisteredTgIdErrorView(Update update, AppUser currentAppUser) {
        currentAppUser.getDepartment().setCurrentView(EMPLOYEES_MANAGEMENT_VIEW);
        appUserRepo.save(currentAppUser);

        return telegramView.alreadyRegisteredTelegramUserIdErrorView(update, currentAppUser);
    }

    private SendMessage invalidEmployeeTgIdErrorView(Update update) {
        return telegramView.invalidEmployeeTgIdErrorView(update);
    }
}

package org.klozevitz.services.implementations.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.DepartmentTelegramView;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.enitites.appUsers.Employee;
import org.klozevitz.enitites.appUsers.enums.states.DepartmentState;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.services.interfaces.updateProcessors.Registrar;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.regex.Pattern;

import static org.klozevitz.enitites.appUsers.enums.states.EmployeeState.BASIC_STATE;
import static org.klozevitz.enitites.appUsers.enums.views.DepartmentView.EMPLOYEE_REGISTRATION_RESULT_VIEW;

@Log4j
@Service
@RequiredArgsConstructor
public class EmployeeRegistrar implements Registrar {
    private final String ALREADY_REGISTERED_TG_ID_ERROR_MESSAGE = "<b>Введенный Телеграм-id уже " +
            "зарегистрирован в системе</b>";
    private final String INVALID_EMPLOYEE_TG_ID_ERROR_MESSAGE = "Введенная строка не является " +
            "корректным telegramUserId";
    private final String EMPLOYEE_REGISTRATION_NOTIFICATION_MESSAGE = "<b>Телеграм-id \"%s\", " +
            "нового сотрудника зарегистрирован. " +
            "Вы можете отредактировать/удалить его в меню менеджмента персонала.</b>";

    private final AppUserRepo appUserRepo;
    private final DepartmentTelegramView telegramView;

    @Override
    public SendMessage register(Update update) {
        var employeeTgId = update.getMessage().getText();
        var isTgIdValid = isTgIdValid(employeeTgId);
        var telegramUserId = telegramUserId(update);
        var currentAppUser = appUserRepo.findByTelegramUserId(telegramUserId).get();
        String message;

        if (isTgIdValid) {
            var success = registerEmployee(currentAppUser, Long.parseLong(employeeTgId));
            message = success ?
                    String.format(EMPLOYEE_REGISTRATION_NOTIFICATION_MESSAGE, employeeTgId) :
                    ALREADY_REGISTERED_TG_ID_ERROR_MESSAGE;
        } else {
            message = INVALID_EMPLOYEE_TG_ID_ERROR_MESSAGE;
        }

        return employeeRegistrationResultView(update, currentAppUser, message);
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

            // TODO ОБЯЗАТЕЛЬНО ПРОВЕРИТЬ!!!!!! до этого работало
//            appUserRepo.save(persistentEmployeeAppUser);
            appUserRepo.save(currentAppUser);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private SendMessage employeeRegistrationResultView(Update update, AppUser currentAppUser, String message) {
        currentAppUserActualState(currentAppUser);
        return telegramView.employeeRegistrationResultView(update, message);
    }

    private void currentAppUserActualState(AppUser currentAppUser) {
        currentAppUser.getDepartment().setCurrentView(EMPLOYEE_REGISTRATION_RESULT_VIEW);
        currentAppUser.getDepartment().setState(DepartmentState.BASIC_STATE);
        appUserRepo.save(currentAppUser);
    }

    private boolean isTgIdValid(String tgId) {
        var regexp = "\\b\\d{8,13}\\b";
        var pattern = Pattern.compile(regexp);
        var matcher = pattern.matcher(tgId);

        return matcher.matches();
    }

    protected long telegramUserId(Update update) {
        return update.hasMessage() ?
                update.getMessage().getFrom().getId() :
                update.getCallbackQuery().getFrom().getId();
    }
}

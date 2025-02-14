package org.klozevitz.services.implementations.utils;

import lombok.RequiredArgsConstructor;
import org.klozevitz.CompanyTelegramView;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.enitites.appUsers.Department;
import org.klozevitz.enitites.appUsers.enums.states.CompanyState;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.services.interfaces.utils.DepartmentActivator;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.regex.Pattern;

import static org.klozevitz.enitites.appUsers.enums.states.DepartmentState.BASIC_STATE;
import static org.klozevitz.enitites.appUsers.enums.views.CompanyView.DEPARTMENTS_MANAGEMENT_VIEW;

@Service
@RequiredArgsConstructor
public class DepartmentActivatorService implements DepartmentActivator {
    private final CompanyTelegramView telegramView;
    private final AppUserRepo appUserRepo;


    @Override
    public SendMessage registerDepartment(Update update, AppUser currentAppUser) {
        var departmentTgId = update.getMessage().getText();
        var isTgIdValid = isTgIdValid(departmentTgId);

        if (isTgIdValid) {
            var success = registerDepartment(currentAppUser, Long.parseLong(departmentTgId));

            return success ?
                    departmentRegistrationNotificationView(update, currentAppUser) :
                    alreadyRegisteredTelegramUserIdErrorView(update, currentAppUser);
        }
        return invalidDepartmentTgIdErrorView(update);
    }

    private boolean registerDepartment(AppUser currentAppUser, long telegramUserId) {
        var persistentCompany = currentAppUser.getCompany();
        var transientDepartmentAppUser = AppUser.builder()
                .telegramUserId(telegramUserId)
                .build();
        var transientDepartment = Department.builder()
                .appUser(transientDepartmentAppUser)
                .company(persistentCompany)
                .state(BASIC_STATE)
                .build();

        transientDepartmentAppUser.setDepartment(transientDepartment);

        try {
            var persistentDepartmentAppUser = appUserRepo.save(transientDepartmentAppUser);

            persistentCompany.getDepartments().add(persistentDepartmentAppUser.getDepartment());
            currentAppUser.getCompany().setCurrentView(DEPARTMENTS_MANAGEMENT_VIEW);

            appUserRepo.save(persistentDepartmentAppUser);
            appUserRepo.save(currentAppUser);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private SendMessage departmentRegistrationNotificationView(Update update, AppUser currentAppUser) {
        currentAppUser.getCompany().setCurrentView(DEPARTMENTS_MANAGEMENT_VIEW);
        currentAppUser.getCompany().setState(CompanyState.BASIC_STATE);
        appUserRepo.save(currentAppUser);

        return telegramView.newDepartmentRegistrationNotificationView(update, currentAppUser);
    }

    private boolean isTgIdValid(String departmentTgId) {
        var regexp = "\\b\\d{8,12}\\b";
        var pattern = Pattern.compile(regexp);
        var matcher = pattern.matcher(departmentTgId);

        return matcher.matches();
    }

    private SendMessage invalidDepartmentTgIdErrorView(Update update) {
        return telegramView.invalidDepartmentTelegramUserIdErrorView(update);
    }

    private SendMessage alreadyRegisteredTelegramUserIdErrorView(Update update, AppUser currentAppUser) {
        currentAppUser.getCompany().setCurrentView(DEPARTMENTS_MANAGEMENT_VIEW);
        appUserRepo.save(currentAppUser);

        return telegramView.alreadyRegisteredTelegramUserIdErrorView(update, currentAppUser);
    }
}

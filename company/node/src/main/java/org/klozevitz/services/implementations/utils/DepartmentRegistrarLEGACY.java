package org.klozevitz.services.implementations.utils;

import lombok.RequiredArgsConstructor;
import org.klozevitz.CompanyTelegramView;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.enitites.appUsers.Department;
import org.klozevitz.enitites.appUsers.enums.states.CompanyState;
import org.klozevitz.logger.LoggerInfo;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.services.util.Registrar_LEGACY;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.klozevitz.enitites.appUsers.enums.states.DepartmentState.BASIC_STATE;
import static org.klozevitz.enitites.appUsers.enums.views.CompanyView.DEPARTMENTS_MANAGEMENT_VIEW;

@RequiredArgsConstructor
public class DepartmentRegistrarLEGACY implements Registrar_LEGACY {
    private final AppUserRepo appUserRepo;
    private final CompanyTelegramView telegramView;
    private final LoggerInfo loggerInfo;


    @Override
    public SendMessage register(Update update, AppUser currentAppUser) {
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

    private SendMessage invalidDepartmentTgIdErrorView(Update update) {
        loggerInfo.LoggerErrorInvalidDepartmentTelegramUserId(update);
        return telegramView.invalidDepartmentTelegramUserIdErrorView(update);
    }

    private SendMessage alreadyRegisteredTelegramUserIdErrorView(Update update, AppUser currentAppUser) {
        currentAppUser.getCompany().setCurrentView(DEPARTMENTS_MANAGEMENT_VIEW);
        appUserRepo.save(currentAppUser);
        loggerInfo.LoggerErrorAlreadyRegisteredTelegramUserId(update);

        return telegramView.alreadyRegisteredTelegramUserIdErrorView(update, currentAppUser);
    }
}

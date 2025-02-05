package org.klozevitz.services.implementations.utils;

import lombok.RequiredArgsConstructor;
import org.klozevitz.TelegramView;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.enitites.appUsers.Department;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.services.interfaces.utils.DepartmentActivator;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.regex.Pattern;

import static org.klozevitz.enitites.appUsers.enums.states.DepartmentState.BASIC_STATE;
import static org.klozevitz.enitites.appUsers.enums.views.CompanyView.DEPARTMENTS_MANAGEMENT_VIEW;
import static org.klozevitz.enitites.appUsers.enums.views.CompanyView.DEPARTMENT_TELEGRAM_USER_ID_REQUEST_VIEW;

@Service
@RequiredArgsConstructor
public class DepartmentActivatorService implements DepartmentActivator {
    private final TelegramView telegramView;
    private final AppUserRepo appUserRepo;


    @Override
    public SendMessage registerDepartment(Update update, AppUser currentAppUser) {
        var departmentTelegramUserId = update.getMessage().getText();
        var isTgUserIdValid = telegramUserIdValidation(departmentTelegramUserId);

        if (isTgUserIdValid) {
            var success = addDepartment(currentAppUser, Long.parseLong(departmentTelegramUserId));
            if (success) {
                return departmentRegistrationNotificationView(update, currentAppUser);
            } else {
                // тут может быть null из-за невозможности сохранить департамент
                return null;
            }
        }
        return invalidDepartmentTelegramUserIdView(update);
    }

    private SendMessage departmentRegistrationNotificationView(Update update, AppUser currentAppUser) {
        currentAppUser.getCompany().setCurrentView(DEPARTMENTS_MANAGEMENT_VIEW);
        appUserRepo.save(currentAppUser);

        return telegramView.departmentRegistrationNotificationView(update, currentAppUser);
    }

    private boolean telegramUserIdValidation(String departmentTelegramUserId) {
        var regexp = "\\b\\d{8,9}\\b";
        var pattern = Pattern.compile(regexp);
        var matcher = pattern.matcher(departmentTelegramUserId);

        return matcher.matches();
    }

    private boolean addDepartment(AppUser currentAppUser, long telegramUserId) {
        var persistentCompany = currentAppUser.getCompany();
        var transientDepartmentAppUser = AppUser.builder()
                .telegramUserId(telegramUserId)
                .department(
                        Department.builder()
                                .state(BASIC_STATE)
                                .company(persistentCompany)
                                .build()
                )
                .build();
        transientDepartmentAppUser.getDepartment().setAppUser(transientDepartmentAppUser);

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

    private SendMessage invalidDepartmentTelegramUserIdView(Update update) {
        return telegramView.invalidDepartmentTelegramUserIdView(update);
    }
}

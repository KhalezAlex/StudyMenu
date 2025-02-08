package org.klozevitz.services.implementations.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.DepartmentTelegramView;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.services.uitl.Registrar;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.inject.Inject;

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
                    alreadyRegisteredTgUserIdErrorView(update, currentAppUser);
        }

        return invalidEmployeeTgIdErrorView(update);
    }

    private boolean registerEmployee(AppUser currentAppUser, long parseLong) {
        return false;
    }

    private SendMessage employeeRegistrationNotificationView(Update update, AppUser currentAppUser) {
        return null;
    }

    private SendMessage alreadyRegisteredTgUserIdErrorView(Update update, AppUser currentAppUser) {
        return null;
    }

    private SendMessage invalidEmployeeTgIdErrorView(Update update) {
        return null;
    }
}

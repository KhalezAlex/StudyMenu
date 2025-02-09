package org.klozevitz.services.implementations.updateProcessors.textUpdateProcessors.byState;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.DepartmentTelegramView;
import org.klozevitz.services.messageProcessors.UpdateProcessor;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.services.uitl.Registrar;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.inject.Inject;

@Log4j
@RequiredArgsConstructor
public class WaitForTgIdStateTUP implements UpdateProcessor {
    private final DepartmentTelegramView telegramView;
    @Inject
    private AppUserRepo appUserRepo;
    @Inject
    private Registrar employeeRegistrar;

    @Override
    public SendMessage processUpdate(Update update, AppUser currentAppUser) {
        var state = currentAppUser.getDepartment().getState();

        switch (state) {
            case WAIT_FOR_EMPLOYEE_TG_ID_STATE:
                return registerEmployee(update, currentAppUser);
            default:
                return telegramView.previousView(update, currentAppUser);
        }
    }

    private SendMessage registerEmployee(Update update, AppUser currentAppUser) {
        return employeeRegistrar.register(update, currentAppUser);
    }
}

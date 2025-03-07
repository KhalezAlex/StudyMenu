package org.klozevitz.services.implementations.updateProcessors.callbackQueryUpdateProcessors.byState;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.DepartmentTelegramView;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.enitites.appUsers.enums.states.DepartmentState;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.services.messageProcessors.UpdateProcessor_LEGACY;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.klozevitz.enitites.appUsers.enums.views.DepartmentView.WELCOME_VIEW;

@Log4j
@RequiredArgsConstructor
public class WaitForEmployeeTgIdStateDepartmentCQUP implements UpdateProcessor_LEGACY {
    private final AppUserRepo appUserRepo;
    private final DepartmentTelegramView telegramView;

    @Override
    public SendMessage processUpdate(Update update, AppUser currentAppUser) {
        var command = update.getCallbackQuery().getData();

        switch (command) {
            case "/start":
                return welcomeView(update, currentAppUser);
            default:
                return telegramView.previousView(update, currentAppUser);
        }
    }

    private SendMessage welcomeView(Update update, AppUser currentAppUser) {
        currentAppUser.getDepartment().setState(DepartmentState.BASIC_STATE);
        currentAppUser.getDepartment().setCurrentView(WELCOME_VIEW);
        appUserRepo.save(currentAppUser);

        return telegramView.welcomeView(update);
    }
}

package org.klozevitz.services.implementations.updateProcessors.callbackQueryUpdateProcessors.byState;

import lombok.RequiredArgsConstructor;
import org.klozevitz.CompanyTelegramView;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.services.messageProcessors.UpdateProcessor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.klozevitz.enitites.appUsers.enums.states.CompanyState.BASIC_STATE;
import static org.klozevitz.enitites.appUsers.enums.views.CompanyView.REGISTERED_WELCOME_VIEW;

@RequiredArgsConstructor
public class WaitForDepartmentTgIdStateCompanyCQUP implements UpdateProcessor {
    private final AppUserRepo appUserRepo;
    private final CompanyTelegramView telegramView;


    @Override
    public SendMessage processUpdate(Update update, AppUser currentAppUser) {
        var command = update.getCallbackQuery().getData();

        switch (command) {
            case "/start" :
                return registeredWelcomeView(update, currentAppUser);
            default:
                return defaultOption(update, currentAppUser);
        }
    }

    private SendMessage registeredWelcomeView(Update update, AppUser currentAppUser) {
        currentAppUser.getCompany().setCurrentView(REGISTERED_WELCOME_VIEW);
        currentAppUser.getCompany().setState(BASIC_STATE);
        appUserRepo.save(currentAppUser);

        return telegramView.registeredWelcomeView(update);
    }

    private SendMessage defaultOption(Update update, AppUser currentAppUser) {
        currentAppUser.getCompany().setCurrentView(REGISTERED_WELCOME_VIEW);
        currentAppUser.getCompany().setState(BASIC_STATE);
        appUserRepo.save(currentAppUser);

        return telegramView.registeredWelcomeView(update);
    }
}

package org.klozevitz.services.implementations.updateProcessors.callbackQueryUpdateProcessors.byState;

import lombok.RequiredArgsConstructor;
import org.klozevitz.CompanyTelegramView;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.services.interfaces.updateProcessors.callbackQueryUpdateProcessors.WaitingForDepartmentTelegramUserIdStateCQUP;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.klozevitz.enitites.appUsers.enums.states.CompanyState.BASIC_STATE;
import static org.klozevitz.enitites.appUsers.enums.views.CompanyView.REGISTERED_WELCOME_VIEW;

@Service
@RequiredArgsConstructor
public class WaitingForDepartmentTelegramUserIdStateCQUP_Service implements WaitingForDepartmentTelegramUserIdStateCQUP {
    private final CompanyTelegramView telegramView;
    private final AppUserRepo appUserRepo;

    @Override
    public SendMessage processCallbackQueryUpdate(Update update, AppUser currentAppUser) {
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

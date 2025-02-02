package org.klozevitz.services.implementations.messageProcessors.callbackQueryMessageProcessors.byState;

import lombok.RequiredArgsConstructor;
import org.klozevitz.TelegramView;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.services.interfaces.messageProcessors.callbackQueryMessageProcessors.BasicStateCQMP;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.klozevitz.enitites.appUsers.enums.views.CompanyView.REGISTERED_WELCOME_VIEW;

@Service
@RequiredArgsConstructor
public class BasicStateCQMP_Service implements BasicStateCQMP {
    private final TelegramView telegramView;
    private final AppUserRepo appUserRepo;

    @Override
    public SendMessage processCallbackQueryMessage(Update update, AppUser currentAppUser) {
        var command = update.getCallbackQuery().getData();

        switch (command) {
            case "/start":
                currentAppUser.getCompany().setCurrentView(REGISTERED_WELCOME_VIEW);
                appUserRepo.save(currentAppUser);
                return telegramView.registeredWelcomeView(update);
            default:
                return telegramView.previousView(update, currentAppUser);
        }
    }
}

package org.klozevitz.services.implementations.updateProcessors.commandUpdateProcessors.byState;

import lombok.RequiredArgsConstructor;
import org.klozevitz.CompanyTelegramView;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.services.messageProcessors.UpdateProcessor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.klozevitz.enitites.appUsers.enums.views.CompanyView.REGISTERED_WELCOME_VIEW;

@Service
@RequiredArgsConstructor
public class BasicStateCompanyCUP implements UpdateProcessor {
    private final AppUserRepo appUserRepo;
    private final CompanyTelegramView telegramView;


    @Override
    public SendMessage processUpdate(Update update, AppUser currentAppUser) {
        var command = update.getMessage().getText();

        switch (command) {
            case "/start":
                return registeredWelcomeView(update, currentAppUser);
            default:
                return telegramView.previousView(update, currentAppUser);
        }
    }

    private SendMessage registeredWelcomeView(Update update, AppUser currentAppUser) {
        currentAppUser.getCompany().setCurrentView(REGISTERED_WELCOME_VIEW);
        appUserRepo.save(currentAppUser);

        return telegramView.registeredWelcomeView(update);
    }
}

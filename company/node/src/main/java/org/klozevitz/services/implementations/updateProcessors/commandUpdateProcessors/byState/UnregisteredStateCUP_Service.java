package org.klozevitz.services.implementations.updateProcessors.commandUpdateProcessors.byState;

import lombok.RequiredArgsConstructor;
import org.klozevitz.TelegramView;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.enitites.appUsers.enums.views.CompanyView;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.services.interfaces.updateProcessors.commandUpdateProcessors.UnregisteredStateCUP;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.klozevitz.enitites.appUsers.enums.views.CompanyView.UNREGISTERED_WELCOME_VIEW;

@Service
@RequiredArgsConstructor
public class UnregisteredStateCUP_Service implements UnregisteredStateCUP {
    private final TelegramView telegramView;
    private final AppUserRepo appUserRepo;

    @Override
    public SendMessage processCommandMessage(Update update, AppUser currentAppUser) {
        var command = update.getMessage().getText();

        switch (command) {
            case "/start":
                return unregisteredWelcomeView(update, currentAppUser);
            default:
                return telegramView.previousView(update, currentAppUser);
        }
    }

    private SendMessage unregisteredWelcomeView(Update update, AppUser currentAppUser) {
        currentAppUser.getCompany().setCurrentView(UNREGISTERED_WELCOME_VIEW);
        appUserRepo.save(currentAppUser);

        return telegramView.unregisteredWelcomeView(update);
    }
}

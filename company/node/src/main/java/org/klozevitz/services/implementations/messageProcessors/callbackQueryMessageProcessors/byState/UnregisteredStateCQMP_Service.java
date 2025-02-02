package org.klozevitz.services.implementations.messageProcessors.callbackQueryMessageProcessors.byState;

import lombok.RequiredArgsConstructor;
import org.klozevitz.TelegramView;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.enitites.appUsers.enums.states.CompanyState;
import org.klozevitz.enitites.appUsers.enums.views.CompanyView;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.services.interfaces.messageProcessors.callbackQueryMessageProcessors.UnregisteredStateCQMP;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.klozevitz.enitites.appUsers.enums.states.CompanyState.WAITING_FOR_EMAIL_STATE;
import static org.klozevitz.enitites.appUsers.enums.views.CompanyView.EMAIL_REQUEST_VIEW;

@Service
@RequiredArgsConstructor
public class UnregisteredStateCQMP_Service implements UnregisteredStateCQMP {
    private final TelegramView telegramView;
    private final AppUserRepo appUserRepo;

    @Override
    public SendMessage processCallbackQueryMessage(Update update, AppUser currentAppUser) {
        var command = update.getCallbackQuery().getData();

        switch (command) {
            case "/register":
                return emailRequestView(update, currentAppUser);
            default:
                return telegramView.previousView(update, currentAppUser);
        }
    }

    private SendMessage emailRequestView(Update update, AppUser currentAppUser) {
        currentAppUser.getCompany().setState(WAITING_FOR_EMAIL_STATE);
        currentAppUser.getCompany().setCurrentView(EMAIL_REQUEST_VIEW);
        appUserRepo.save(currentAppUser);

        return telegramView.emailRequestView(update);
    }
}

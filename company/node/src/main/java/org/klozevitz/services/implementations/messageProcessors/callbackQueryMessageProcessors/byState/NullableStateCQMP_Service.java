package org.klozevitz.services.implementations.messageProcessors.callbackQueryMessageProcessors.byState;

import lombok.RequiredArgsConstructor;
import org.klozevitz.TelegramView;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.services.interfaces.messageProcessors.callbackQueryMessageProcessors.NullableStateCQMP;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.klozevitz.enitites.appUsers.enums.states.CompanyState.WAITING_FOR_EMAIL_STATE;
import static org.klozevitz.enitites.appUsers.enums.views.CompanyView.EMAIL_REQUEST_VIEW;

@Service
@RequiredArgsConstructor
public class NullableStateCQMP_Service implements NullableStateCQMP {
    private final TelegramView telegramView;
    private final AppUserRepo appUserRepo;


    /**
     * TODO он вообще не должен выскакивать- просто написать сообщение, чтобы связались с службой поддержки
     * */

    @Override
    public SendMessage processCallbackQueryMessage(Update update, AppUser currentAppUser) {
        var command = update.getCallbackQuery().getData();

        switch (command) {
            default:
                return null;
        }
    }

    private SendMessage emailRequestView(Update update, AppUser currentAppUser) {
        currentAppUser.getCompany().setState(WAITING_FOR_EMAIL_STATE);
        currentAppUser.getCompany().setCurrentView(EMAIL_REQUEST_VIEW);
        appUserRepo.save(currentAppUser);

        return telegramView.emailRequestView(update);
    }
}

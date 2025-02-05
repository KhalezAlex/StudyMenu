package org.klozevitz.services.implementations.updateProcessors.callbackQueryUpdateProcessors.byState;

import lombok.RequiredArgsConstructor;
import org.klozevitz.MessageUtil;
import org.klozevitz.TelegramView;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.services.interfaces.updateProcessors.callbackQueryUpdateProcessors.WaitingForEmailStateCQUP;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.klozevitz.enitites.appUsers.enums.states.CompanyState.WAITING_FOR_EMAIL_STATE;
import static org.klozevitz.enitites.appUsers.enums.views.CompanyView.EMAIL_REQUEST_VIEW;

@Service
@RequiredArgsConstructor
public class WaitingForEmailStateCQUP_Service implements WaitingForEmailStateCQUP {
    private final String REGISTRATION_ABORT_NOTIFICATION_MESSAGE = "Регистрация была прервана";
    private final TelegramView telegramView;
    private final AppUserRepo appUserRepo;
    private final MessageUtil messageUtil;

    @Override
    public SendMessage processCallbackQueryMessage(Update update, AppUser currentAppUser) {
        var command = update.getCallbackQuery().getData();

        switch (command) {
            case "abort_registration" :
                return abortRegistration(update, currentAppUser);
            default:
                return null;
        }
    }

    private SendMessage abortRegistration(Update update, AppUser currentAppUser) {
        var answer = telegramView.emailRequestView(update);

        currentAppUser.getCompany().setEmail(null);
        currentAppUser.getCompany().setState(WAITING_FOR_EMAIL_STATE);
        currentAppUser.getCompany().setCurrentView(EMAIL_REQUEST_VIEW);
        appUserRepo.save(currentAppUser);

        return messageUtil.addServiceMessage(answer, REGISTRATION_ABORT_NOTIFICATION_MESSAGE);
    }
}

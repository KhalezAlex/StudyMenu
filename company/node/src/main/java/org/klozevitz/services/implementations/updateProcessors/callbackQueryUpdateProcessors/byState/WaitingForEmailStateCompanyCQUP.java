package org.klozevitz.services.implementations.updateProcessors.callbackQueryUpdateProcessors.byState;

import lombok.RequiredArgsConstructor;
import org.klozevitz.MessageUtil;
import org.klozevitz.CompanyTelegramView;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.services.messageProcessors.UpdateProcessor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.klozevitz.enitites.appUsers.enums.states.CompanyState.WAIT_FOR_EMAIL_STATE;
import static org.klozevitz.enitites.appUsers.enums.views.CompanyView.EMAIL_REQUEST_VIEW;

@Service
@RequiredArgsConstructor
public class WaitingForEmailStateCompanyCQUP implements UpdateProcessor {
    private final String REGISTRATION_ABORT_NOTIFICATION_MESSAGE = "Регистрация была прервана";
    private final AppUserRepo appUserRepo;
    private final CompanyTelegramView telegramView;
    private final MessageUtil messageUtil;

    @Override
    public SendMessage processUpdate(Update update, AppUser currentAppUser) {
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
        currentAppUser.getCompany().setState(WAIT_FOR_EMAIL_STATE);
        currentAppUser.getCompany().setCurrentView(EMAIL_REQUEST_VIEW);
        appUserRepo.save(currentAppUser);

        return messageUtil.addServiceMessage(answer, REGISTRATION_ABORT_NOTIFICATION_MESSAGE);
    }
}

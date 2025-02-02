package org.klozevitz.services.implementations.messageProcessors.textMessageProcessors;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.MessageUtil;
import org.klozevitz.TelegramView;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.messageProcessors.TextMessageProcessor;
import org.klozevitz.services.interfaces.utils.CompanyActivator;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;


@Log4j
@Service
@RequiredArgsConstructor
public class TextMessageProcessorService implements TextMessageProcessor {
    private final TelegramView telegramView;
    private final MessageUtil messageUtil;
    private final CompanyActivator companyActivator;

    @Override
    public SendMessage processTextMessage(Update update, AppUser currentAppUser) {
        var state = currentAppUser.getCompany().getState();

        if (state == null) {
            var answer = telegramView.unregisteredWelcomeView(update);
            return messageUtil.addErrorMessage(answer, "Что-то пошло не так, а пока...");
        }

        switch (state) {
            case WAITING_FOR_EMAIL_STATE: return companyActivator.setEmail(update, currentAppUser);
            case WAITING_FOR_DEPARTMENT_TELEGRAM_USER_ID_STATE: return null;
            default: return null;
        }
    }
}
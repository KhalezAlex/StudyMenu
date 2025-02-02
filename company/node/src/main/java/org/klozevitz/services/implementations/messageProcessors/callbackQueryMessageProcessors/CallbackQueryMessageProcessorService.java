package org.klozevitz.services.implementations.messageProcessors.callbackQueryMessageProcessors;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.TelegramView;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.messageProcessors.CallbackQueryMessageProcessor;
import org.klozevitz.services.interfaces.messageProcessors.callbackQueryMessageProcessors.BasicStateCQMP;
import org.klozevitz.services.interfaces.messageProcessors.callbackQueryMessageProcessors.NullableStateCQMP;
import org.klozevitz.services.interfaces.messageProcessors.callbackQueryMessageProcessors.UnregisteredStateCQMP;
import org.klozevitz.services.interfaces.messageProcessors.callbackQueryMessageProcessors.WaitingForEmailStateCQMP;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Log4j
@Service("callbackQueryMessageProcessor")
@RequiredArgsConstructor
public class CallbackQueryMessageProcessorService implements CallbackQueryMessageProcessor {
    private final TelegramView telegramView;
    private final NullableStateCQMP nullableStateCQMP;
    private final UnregisteredStateCQMP unregisteredStateCQMP;
    private final WaitingForEmailStateCQMP waitingForEmailStateCQMP;
    private final BasicStateCQMP basicStateCQMP;

    @Override
    public SendMessage processCallbackQueryMessage(Update update, AppUser currentAppUser) {
        var state = currentAppUser.getCompany().getState();

        if (state == null) {
            return nullableStateCQMP.processCallbackQueryMessage(update, currentAppUser);
        }

        switch (state) {
            case UNREGISTERED_STATE:
                return unregisteredStateCQMP.processCallbackQueryMessage(update, currentAppUser);
            case WAITING_FOR_EMAIL_CONFIRMATION_STATE:
                return waitingForEmailStateCQMP.processCallbackQueryMessage(update, currentAppUser);
            case BASIC_STATE:
                return basicStateCQMP.processCallbackQueryMessage(update, currentAppUser);
            default: {
                log.error("Сообщение не попало ни в одну из веток состояний компании");
                return telegramView.previousView(update, currentAppUser);
            }
        }
    }


}

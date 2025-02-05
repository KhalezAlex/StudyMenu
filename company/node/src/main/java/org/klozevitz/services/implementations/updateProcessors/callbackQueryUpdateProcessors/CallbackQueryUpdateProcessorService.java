package org.klozevitz.services.implementations.updateProcessors.callbackQueryUpdateProcessors;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.TelegramView;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.messageProcessors.CallbackQueryUpdateProcessor;
import org.klozevitz.services.interfaces.updateProcessors.callbackQueryUpdateProcessors.BasicStateCQUP;
import org.klozevitz.services.interfaces.updateProcessors.NullableStateUpdateProcessor;
import org.klozevitz.services.interfaces.updateProcessors.callbackQueryUpdateProcessors.UnregisteredStateCQUP;
import org.klozevitz.services.interfaces.updateProcessors.callbackQueryUpdateProcessors.WaitingForEmailStateCQUP;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Log4j
@Service("callbackQueryUpdateProcessor")
@RequiredArgsConstructor
public class CallbackQueryUpdateProcessorService implements CallbackQueryUpdateProcessor {
    private final TelegramView telegramView;
    private final NullableStateUpdateProcessor nullableStateUpdateProcessor;
    private final UnregisteredStateCQUP unregisteredStateCQUP;
    private final WaitingForEmailStateCQUP waitingForEmailStateCQUP;
    private final BasicStateCQUP basicStateCQUP;

    @Override
    public SendMessage processCallbackQueryMessage(Update update, AppUser currentAppUser) {
        var state = currentAppUser.getCompany().getState();

        if (state == null) {
            return nullableStateUpdateProcessor.processUpdate(update, currentAppUser);
        }

        switch (state) {
            case UNREGISTERED_STATE:
                return unregisteredStateCQUP.processCallbackQueryMessage(update, currentAppUser);
            case WAITING_FOR_EMAIL_CONFIRMATION_STATE:
                return waitingForEmailStateCQUP.processCallbackQueryMessage(update, currentAppUser);
            case BASIC_STATE:
                return basicStateCQUP.processCallbackQueryMessage(update, currentAppUser);
            default: {
                log.error("Сообщение не попало ни в одну из веток состояний компании");
                return telegramView.previousView(update, currentAppUser);
            }
        }
    }


}

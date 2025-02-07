package org.klozevitz.services.implementations.updateProcessors.callbackQueryUpdateProcessors;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.CompanyTelegramView;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.messageProcessors.legacy.CallbackQueryUpdateProcessor;
import org.klozevitz.services.interfaces.updateProcessors.callbackQueryUpdateProcessors.BasicStateCQUP;
import org.klozevitz.messageProcessors.legacy.utils.NullableStateUpdateProcessor;
import org.klozevitz.services.interfaces.updateProcessors.callbackQueryUpdateProcessors.UnregisteredStateCQUP;
import org.klozevitz.services.interfaces.updateProcessors.callbackQueryUpdateProcessors.WaitingForDepartmentTelegramUserIdStateCQUP;
import org.klozevitz.services.interfaces.updateProcessors.callbackQueryUpdateProcessors.WaitingForEmailStateCQUP;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Log4j
@Service("callbackQueryUpdateProcessor")
@RequiredArgsConstructor
public class CallbackQueryUpdateProcessorService implements CallbackQueryUpdateProcessor {
    private final CompanyTelegramView telegramView;
    private final NullableStateUpdateProcessor nullableStateUpdateProcessor;
    private final UnregisteredStateCQUP unregisteredStateCQUP;
    private final WaitingForEmailStateCQUP waitingForEmailStateCQUP;
    private final BasicStateCQUP basicStateCQUP;
    private final WaitingForDepartmentTelegramUserIdStateCQUP waitingForDepartmentTelegramUserIdStateCQUP;

    @Override
    public SendMessage processCallbackQueryUpdate(Update update, AppUser currentAppUser) {
        var state = currentAppUser.getCompany().getState();

        if (state == null) {
            return nullableStateUpdateProcessor.processUpdate(update, currentAppUser);
        }

        switch (state) {
            case UNREGISTERED_STATE:
                return unregisteredStateCQUP.processCallbackQueryUpdate(update, currentAppUser);
            case WAITING_FOR_EMAIL_CONFIRMATION_STATE:
                return waitingForEmailStateCQUP.processCallbackQueryUpdate(update, currentAppUser);
            case BASIC_STATE:
                return basicStateCQUP.processCallbackQueryUpdate(update, currentAppUser);
            case WAITING_FOR_DEPARTMENT_TELEGRAM_USER_ID_STATE:
                return waitingForDepartmentTelegramUserIdStateCQUP.processCallbackQueryUpdate(update, currentAppUser);
            default: {
                log.error("Сообщение не попало ни в одну из веток состояний компании");
                return telegramView.previousView(update, currentAppUser);
            }
        }
    }


}

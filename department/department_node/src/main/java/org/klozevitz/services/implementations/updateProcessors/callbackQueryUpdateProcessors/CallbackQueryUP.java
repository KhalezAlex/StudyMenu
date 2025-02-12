package org.klozevitz.services.implementations.updateProcessors.callbackQueryUpdateProcessors;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.services.messageProcessors.UpdateProcessor;
import org.klozevitz.enitites.appUsers.AppUser;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.annotation.Resource;

@Log4j
@RequiredArgsConstructor
public class CallbackQueryUP implements UpdateProcessor {
    @Resource(name = "nullableState_UpdateProcessor")
    private UpdateProcessor nullableStateUpdateProcessor;
    @Resource(name = "previousView_UpdateProcessor")
    private UpdateProcessor previousViewUpdateProcessor;
    @Resource(name = "basicState_CallbackQuery_UpdateProcessor")
    private UpdateProcessor basicStateCallbackQueryUpdateProcessor;
    @Resource(name = "waitForDocumentState_CallbackQuery_UpdateProcessor")
    private UpdateProcessor waitForDocumentStateCallbackQueryUpdateProcessor;
    @Resource(name = "waitForEmployeeTgIdState_CallbackQuery_UpdateProcessor")
    private UpdateProcessor waitForEmployeeTgIdStateCallbackQueryUpdateProcessor;

    @Override
    public SendMessage processUpdate(Update update, AppUser currentAppUser) {
        var state = currentAppUser.getDepartment().getState();

        if (state == null) {
            return nullableStateUpdateProcessor.processUpdate(update, currentAppUser);
        }

        switch (state) {
            case BASIC_STATE:
                return basicStateCallbackQueryUpdateProcessor.processUpdate(update, currentAppUser);
            case WAIT_FOR_DOCUMENT_STATE:
                return waitForDocumentStateCallbackQueryUpdateProcessor.processUpdate(update, currentAppUser);
            case WAIT_FOR_EMPLOYEE_TG_ID_STATE:
                return waitForEmployeeTgIdStateCallbackQueryUpdateProcessor.processUpdate(update, currentAppUser);
            default:
                return previousViewUpdateProcessor.processUpdate(update, currentAppUser);
        }
    }
}

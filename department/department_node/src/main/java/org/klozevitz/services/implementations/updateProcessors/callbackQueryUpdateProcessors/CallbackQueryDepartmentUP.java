package org.klozevitz.services.implementations.updateProcessors.callbackQueryUpdateProcessors;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.services.messageProcessors.UpdateProcessor_LEGACY;
import org.klozevitz.enitites.appUsers.AppUser;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Log4j
@RequiredArgsConstructor
public class CallbackQueryDepartmentUP implements UpdateProcessor_LEGACY {
    private final UpdateProcessor_LEGACY nullableStateUpdateProcessor;
    private final UpdateProcessor_LEGACY previousViewUpdateProcessor;
    private final UpdateProcessor_LEGACY basicStateCallbackQueryUpdateProcessor;
    private final UpdateProcessor_LEGACY waitForDocumentStateCallbackQueryUpdateProcessor;
    private final UpdateProcessor_LEGACY waitForEmployeeTgIdStateCallbackQueryUpdateProcessor;

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

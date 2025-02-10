package org.klozevitz.services.implementations.updateProcessors.docUpdateProcessors;

import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.services.messageProcessors.UpdateProcessor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.annotation.Resource;

public class DocumentUpdateProcessor implements UpdateProcessor {
    @Resource(name = "nullableStateUpdateProcessor")
    private UpdateProcessor nullableStateUpdateProcessor;

    @Override
    public SendMessage processUpdate(Update update, AppUser currentAppUser) {
        var state = currentAppUser.getDepartment().getState();

        if (state == null) {
            nullableStateUpdateProcessor.processUpdate(update, currentAppUser);
        }

        switch (state) {
            case WAIT_FOR_DOCUMENT_STATE:
                return null;
        }
        return null;
    }
}

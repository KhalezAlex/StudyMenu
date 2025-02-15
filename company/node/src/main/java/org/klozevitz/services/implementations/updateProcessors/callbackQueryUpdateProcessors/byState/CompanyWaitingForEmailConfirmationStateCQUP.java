package org.klozevitz.services.implementations.updateProcessors.callbackQueryUpdateProcessors.byState;

import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.services.messageProcessors.UpdateProcessor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class CompanyWaitingForEmailConfirmationStateCQUP implements UpdateProcessor {
    @Override
    public SendMessage processUpdate(Update update, AppUser currentAppUser) {
        return null;
    }
}

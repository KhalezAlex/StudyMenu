package org.klozevitz.services.implementations.updateProcessors.callbackQueryUpdateProcessors.byState;

import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.services.interfaces.updateProcessors.callbackQueryUpdateProcessors.WaitingForEmailConfirmationStateCQUP;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class WaitingForEmailConfirmationStateCQUP_Service implements WaitingForEmailConfirmationStateCQUP {
    @Override
    public SendMessage processCallbackQueryMessage(Update update, AppUser currentAppUser) {
        return null;
    }
}

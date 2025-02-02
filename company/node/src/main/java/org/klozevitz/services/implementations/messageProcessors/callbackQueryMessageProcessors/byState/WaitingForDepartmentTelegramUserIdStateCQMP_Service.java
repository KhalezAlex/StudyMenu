package org.klozevitz.services.implementations.messageProcessors.callbackQueryMessageProcessors.byState;

import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.services.interfaces.messageProcessors.callbackQueryMessageProcessors.WaitingForDepartmentTelegramUserIdStateCQMP;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class WaitingForDepartmentTelegramUserIdStateCQMP_Service implements WaitingForDepartmentTelegramUserIdStateCQMP {
    @Override
    public SendMessage processCallbackQueryMessage(Update update, AppUser currentAppUser) {
        return null;
    }
}

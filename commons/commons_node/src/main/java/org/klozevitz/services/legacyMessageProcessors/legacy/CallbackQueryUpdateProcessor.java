package org.klozevitz.services.legacyMessageProcessors.legacy;

import org.klozevitz.enitites.appUsers.AppUser;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface CallbackQueryUpdateProcessor {
    SendMessage processCallbackQueryUpdate(Update update, AppUser currentAppUser);
}

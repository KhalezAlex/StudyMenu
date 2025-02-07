package org.klozevitz.messageProcessors.legacy;

import org.klozevitz.enitites.appUsers.AppUser;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface DocUpdateProcessor {
    SendMessage processDocUpdate(Update update, AppUser currentAppUser);
}

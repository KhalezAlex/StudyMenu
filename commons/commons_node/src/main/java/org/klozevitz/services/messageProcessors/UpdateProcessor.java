package org.klozevitz.services.messageProcessors;

import org.klozevitz.enitites.appUsers.AppUser;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateProcessor {
    SendMessage processUpdate(Update update, AppUser currentAppUser);
}

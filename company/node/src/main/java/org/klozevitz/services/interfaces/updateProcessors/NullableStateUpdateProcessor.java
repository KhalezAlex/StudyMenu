package org.klozevitz.services.interfaces.updateProcessors;

import org.klozevitz.enitites.appUsers.AppUser;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface NullableStateUpdateProcessor {
    SendMessage processUpdate(Update update, AppUser currentAppUser);
}

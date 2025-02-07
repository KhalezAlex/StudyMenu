package org.klozevitz.services.interfaces.updateProcessors;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface NotRegisteredAppUserUpdateProcessor {
    SendMessage processUpdate(Update update);
}

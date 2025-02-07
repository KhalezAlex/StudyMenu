package org.klozevitz;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface WrongAppUserDataUpdateProcessor {
    SendMessage processUpdate(Update update);
}

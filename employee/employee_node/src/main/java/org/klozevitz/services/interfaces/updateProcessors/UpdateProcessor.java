package org.klozevitz.services.interfaces.updateProcessors;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateProcessor {
    SendMessage processUpdate(Update update);

    default long telegramUserId(Update update) {
        return update.hasMessage() ?
                update.getMessage().getFrom().getId() :
                update.getCallbackQuery().getFrom().getId();
    }

    default String command(Update update) {
        return update.hasMessage() ?
                update.getMessage().getText() :
                update.getCallbackQuery().getData();
    }
}

package org.klozevitz.services.interfaces.updateProcessors;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

public interface UpdateProcessor {
    ArrayList<SendMessage> processUpdate(Update update);

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

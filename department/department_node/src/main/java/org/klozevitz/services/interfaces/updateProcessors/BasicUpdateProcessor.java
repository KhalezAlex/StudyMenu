package org.klozevitz.services.interfaces.updateProcessors;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;

public abstract class BasicUpdateProcessor implements UpdateProcessor {

    protected long telegramUserId(Update update) {
        return update.hasMessage() ?
                update.getMessage().getFrom().getId() :
                update.getCallbackQuery().getFrom().getId();
    }

    protected ArrayList<SendMessage> answerAsList(SendMessage answer) {
        var answerAsArray = new ArrayList<SendMessage>();
        answerAsArray.add(answer);
        return answerAsArray;
    }
}

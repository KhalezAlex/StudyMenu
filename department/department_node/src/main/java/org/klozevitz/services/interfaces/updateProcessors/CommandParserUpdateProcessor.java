package org.klozevitz.services.interfaces.updateProcessors;

import org.telegram.telegrambots.meta.api.objects.Update;

public abstract class CommandParserUpdateProcessor extends BasicUpdateProcessor {
    protected String command(Update update) {
        return update.hasMessage() ?
                update.getMessage().getText() :
                update.getCallbackQuery().getData();
    }
}

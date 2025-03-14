package org.klozevitz.services.interfaces.main;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateConsumer {
    void consumeTextUpdate(Update update);
    void consumeCommandUpdate(Update update);
    void consumeCallbackQueryUpdate(Update update);
}

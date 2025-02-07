package org.klozevitz.services.interfaces.main;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface Main {
    void processTextMessage(Update update);
    void processCommandMessage(Update update);
    void processCallbackQueryMessage(Update update);
    void processDocMessage(Update update);
}

package org.klozevitz.services.interfaces.main;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface Main {
    void processTextUpdate(Update update);
    void processCommandUpdate(Update update);
    void processCallbackQueryUpdate(Update update);
    void processDocUpdate(Update update);
}

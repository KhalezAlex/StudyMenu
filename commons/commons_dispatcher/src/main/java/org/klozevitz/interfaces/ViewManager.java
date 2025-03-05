package org.klozevitz.interfaces;

import org.telegram.telegrambots.meta.api.objects.Message;

public interface ViewManager {
    void saveMessageId(Message messageSent);
    void flushHistory(long telegramUserId);
}

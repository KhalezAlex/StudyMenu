package org.klozevitz.interfaces;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface ViewManager {
    void saveMessageId(SendMessage answer, int sentMessageId);
    void flushHistory(long telegramUserId);
}

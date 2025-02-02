package org.klozevitz.services.interfaces;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface AnswerProducer {
    void produceAnswer(SendMessage sendMessage);
}

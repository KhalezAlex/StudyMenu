package org.klozevitz.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.controllers.UpdateController;
import org.klozevitz.interfaces.AnswerConsumer;
import org.klozevitz.logger.LoggerInfo;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static org.klozevitz.RabbitQueue.COMPANY_ANSWER_MESSAGE;

@Service
@RequiredArgsConstructor
public class AnswerConsumerService implements AnswerConsumer {
    private final UpdateController updateController;
    private final LoggerInfo loggerInfo;

    @Override
    @RabbitListener(queues = COMPANY_ANSWER_MESSAGE)
    public void consume(SendMessage sendMessage) {
        updateController.setView(sendMessage);
        loggerInfo.LoggerInfoMessage(sendMessage);
    }
}

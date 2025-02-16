package org.klozevitz.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.controllers.UpdateController;
import org.klozevitz.interfaces.AnswerConsumer;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static org.klozevitz.RabbitQueue.EMPLOYEE_ANSWER_MESSAGE;

@Log4j
@Service
@RequiredArgsConstructor
public class AnswerConsumerService implements AnswerConsumer {
    private final UpdateController updateController;

    @Override
    @RabbitListener(queues = EMPLOYEE_ANSWER_MESSAGE)
    public void consume(SendMessage sendMessage) {
        updateController.setView(sendMessage);
    }
}

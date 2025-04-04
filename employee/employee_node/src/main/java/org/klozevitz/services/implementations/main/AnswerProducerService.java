package org.klozevitz.services.implementations.main;

import lombok.RequiredArgsConstructor;
import org.klozevitz.services.interfaces.main.AnswerProducer;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static org.klozevitz.RabbitQueue.EMPLOYEE_ANSWER_MESSAGE;

@Service
@RequiredArgsConstructor
public class AnswerProducerService implements AnswerProducer {
    private final RabbitTemplate rabbitTemplate;
    @Override
    public void produceAnswer(SendMessage answer) {
        rabbitTemplate.convertAndSend(EMPLOYEE_ANSWER_MESSAGE, answer);
    }
}

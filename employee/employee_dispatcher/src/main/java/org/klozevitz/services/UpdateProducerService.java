package org.klozevitz.services;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.interfaces.UpdateProducer;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Log4j
@Service
@AllArgsConstructor
public class UpdateProducerService implements UpdateProducer {
    private final RabbitTemplate rabbitTemplate;
    @Override
    public void produce(String rabbitQueue, Update update) {
        var debugMessage = String.format("message for queue: \"%s\" received", rabbitQueue);
        log.debug(debugMessage);
        rabbitTemplate.convertAndSend(rabbitQueue, update);
    }
}

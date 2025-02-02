package org.klozevitz.services.implementations;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.services.interfaces.Main;
import org.klozevitz.services.interfaces.UpdateConsumer;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.klozevitz.RabbitQueue.*;

@Log4j
@Service
@RequiredArgsConstructor
public class UpdateConsumerService implements UpdateConsumer {
    private final Main main;

    @Override
    @RabbitListener(queues = COMPANY_TEXT_UPDATE)
    public void consumeTextUpdate(Update update) {
        log.debug("text message received");
        main.processTextMessage(update);
    }

    @Override
    @RabbitListener(queues = COMPANY_COMMAND_UPDATE)
    public void consumeCommandUpdate(Update update) {
        log.debug("text command message received");
        main.processCommandMessage(update);
    }

    @Override
    @RabbitListener(queues = COMPANY_CALLBACK_QUERY_UPDATE)
    public void consumeCallbackQueryUpdate(Update update) {
        log.debug("callback query message received");
        main.processCallbackQueryMessage(update);
    }
}

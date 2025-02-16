package org.klozevitz.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.interfaces.UpdateProducer;
import org.klozevitz.telegram.TelegramBotComponent;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.klozevitz.RabbitQueue.*;

@Log4j
@Component
@RequiredArgsConstructor
public class UpdateController {
    private final UpdateProducer updateProducer;
    private TelegramBotComponent bot;

    public void registerBot(TelegramBotComponent bot) {
        this.bot = bot;
    }

    public void processUpdate(Update update) {
        if (update == null) {
            log.error("received update is null");
            return;
        }
        if ((update.hasMessage() && update.getMessage().hasText()) || update.hasCallbackQuery()) {
            distributeMessageByType(update);
        } else {
            log.error("Unsupported received message type " + update);
        }
    }

    private void distributeMessageByType(Update update) {
        if (update.hasMessage()) {
            if (update.getMessage().getText().startsWith("/")) {
                processCommandUpdate(update);
            } else {
                processTextUpdate(update);
            }
        } else {
            processCallBackQueryUpdate(update);
        }
    }

    private void processTextUpdate(Update update) {
        updateProducer.produce(EMPLOYEE_TEXT_UPDATE, update);
    }

    private void processCommandUpdate(Update update) {
        updateProducer.produce(EMPLOYEE_COMMAND_UPDATE, update);
    }

    private void processCallBackQueryUpdate(Update update) {
        updateProducer.produce(EMPLOYEE_CALLBACK_QUERY_UPDATE, update);
    }

    public void setView(SendMessage sendMessage) {
        bot.sendAnswerMessage(sendMessage);
    }
}
package org.klozevitz.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.logger.LoggerInfo;
import org.klozevitz.telegram.TelegramBotComponent;
import org.klozevitz.interfaces.UpdateProducer;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.klozevitz.RabbitQueue.*;

@Log4j
@Component
@RequiredArgsConstructor
public class UpdateController {
    private final UpdateProducer updateProducer;
    private final LoggerInfo loggerInfo;
    private TelegramBotComponent bot;

    public void registerBot(TelegramBotComponent bot) {
        this.bot = bot;
    }

    public void processUpdate(Update update) {
        if (update == null) {
            log.error("received update is null");
            return;
        }

        if (update.hasMessage() || update.hasCallbackQuery()) {
            distributeMessageByType(update);
        } else {
            loggerInfo.LoggerErrorUnsupportedMessageType(update);
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
            processCallbackQueryUpdate(update);
        }
    }

    private void processTextUpdate(Update update) {
        updateProducer.produce(COMPANY_TEXT_UPDATE, update);
    }

    private void processCommandUpdate(Update update) {
        updateProducer.produce(COMPANY_COMMAND_UPDATE, update);
    }

    private void processCallbackQueryUpdate(Update update) {
        updateProducer.produce(COMPANY_CALLBACK_QUERY_UPDATE, update);
    }

    public void setView(SendMessage sendMessage) {
        bot.sendAnswerMessage(sendMessage);
    }
}

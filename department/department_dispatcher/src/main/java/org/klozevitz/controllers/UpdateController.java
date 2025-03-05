package org.klozevitz.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.DepartmentDispatcherApp;
import org.klozevitz.interfaces.UpdateProducer;
import org.klozevitz.telegram.TelegramBotComponent;
import org.klozevitz.telegram_component.DepartmentTelegramBot;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.klozevitz.RabbitQueue.*;

@Log4j
@Component
@RequiredArgsConstructor
public class UpdateController {
    private final UpdateProducer updateProducer;
    private DepartmentTelegramBot bot;

    public void registerBot(DepartmentTelegramBot bot) {
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
            log.error("Unsupported received message type " + update);
        }
    }

    private void distributeMessageByType(Update update) {
        if (update.hasMessage()) {
            if (update.getMessage().hasDocument()) {
                processDocUpdate(update);
                return;
            }
            if (update.getMessage().getText().startsWith("/")) {
                processCommandUpdate(update);
            } else {
                processTextUpdate(update);
            }
        } else {
            processCallbackQueryUpdate(update);
        }
    }

    private void processDocUpdate(Update update) {
        updateProducer.produce(DEPARTMENT_DOC_UPDATE, update);
    }

    private void processTextUpdate(Update update) {
        updateProducer.produce(DEPARTMENT_TEXT_UPDATE, update);
    }

    private void processCommandUpdate(Update update) {
        updateProducer.produce(DEPARTMENT_COMMAND_UPDATE, update);
    }

    private void processCallbackQueryUpdate(Update update) {
        updateProducer.produce(DEPARTMENT_CALLBACK_QUERY_UPDATE, update);
    }

    public void setView(SendMessage sendMessage) {
        bot.sendAnswerMessage(sendMessage);
    }
}

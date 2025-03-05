package org.klozevitz.telegram;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Log4j
@Setter
@RequiredArgsConstructor
public abstract class TelegramBotComponent extends TelegramLongPollingBot {
    private String username;
    private String token;

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    public Message sendAnswerMessage(SendMessage sendMessage) {
        if (sendMessage != null) {
            try {
                return execute(sendMessage);
            } catch (TelegramApiException e) {
                log.error(e);
            }
        }
        return null;
    }
}

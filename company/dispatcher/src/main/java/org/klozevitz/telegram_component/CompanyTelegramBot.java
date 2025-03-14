package org.klozevitz.telegram_component;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.telegram.TelegramBotComponent;
import org.klozevitz.controllers.UpdateController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;

@Log4j
@Component
@RequiredArgsConstructor
public class CompanyTelegramBot extends TelegramBotComponent {
    @Value(value = "${bot.username}")
    private String username;
    @Value(value = "${bot.token}")
    private String token;
    private final UpdateController updateController;

    @PostConstruct
    public void init() {
        super.setUsername(username);
        super.setToken(token);
        updateController.registerBot(this);
    }

    @Override
    public void onUpdateReceived(Update update) {
        log.debug("update received");
        updateController.processUpdate(update);
    }
}

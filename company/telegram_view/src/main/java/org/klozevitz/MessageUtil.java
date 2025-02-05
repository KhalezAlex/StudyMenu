package org.klozevitz;

import lombok.extern.log4j.Log4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Log4j
public class MessageUtil {
    private final String BASIC_WRONG_STATUS_ERROR_MESSAGE = "По какой-то причине, ваш статус не позволяет" +
            " пользоваться этой командой. Пройдите регистрацию заново";

    public SendMessage addServiceMessage(SendMessage answer, String error) {
        var message = String.format("%s\n\n%s", error, answer.getText());
        answer.setText(message);
        return answer;
    }

    public SendMessage blankAnswer(Update update) {
        var answer = new SendMessage();
        var chatId = update.hasMessage() ?
                update.getMessage().getChatId() :
                update.getCallbackQuery().getMessage().getChatId();
        answer.setChatId(chatId);
        return answer;
    }

    public SendMessage blankAnswer(long chatId) {
        var answer = new SendMessage();
        answer.setChatId(chatId);
        return answer;
    }
}

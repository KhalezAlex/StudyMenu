package org.klozevitz.logger;

import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Log4j
@NoArgsConstructor
@Component
public class LoggerInfo implements ILoggerInfo {

    @Override
    public void LoggerInfoUpdate(Update update) {
        String messageInfo = "tgId %s | tgName %s | message %s";
        String messageCallback = "tgId %s | tgName %s | messageCallback %s";
        String messageCommand = "tgId %s | tgName %s | messageCommand %s";
        String messageEdite = "tgId %s | tgName %s | messageEdite %s";
        String messageDocument = "Документ в обработке | tgId %s | tgName %s";
        if (update.hasCallbackQuery()) {
            var callbackQuery = update.getCallbackQuery();
            log.info(String.format(messageCallback,
                    callbackQuery.getFrom().getId(),
                    callbackQuery.getFrom().getUserName(),
                    callbackQuery.getData()));
        } else if (update.hasMessage() && update.getMessage().hasText())  {
            var message = update.getMessage();
            if (update.getMessage().getText().startsWith("/")) {
                log.info(String.format(messageCommand,
                        message.getFrom().getId(),
                        message.getFrom().getUserName(),
                        message.getText()));
            } else {
                log.info(String.format(messageInfo,
                        message.getFrom().getId(),
                        message.getFrom().getUserName(),
                        message.getText()));
            }
        } else if (update.hasEditedMessage()) {
            var editeMessage = update.getEditedMessage();
            log.info(String.format(messageEdite,
                    editeMessage.getFrom().getId(),
                    editeMessage.getFrom().getUserName(),
                    editeMessage.getText()));
        } else if (update.hasMessage() && update.getMessage().hasDocument()) {
            var message = update.getMessage();
            log.info(String.format(messageDocument,
                    message.getFrom().getId(),
                    message.getFrom().getUserName()));
        }
    }
    @Override
    public void LoggerInfoMessage(SendMessage message) {
        String massageInfo = "message %s | tgId %s";
        log.info(String.format(massageInfo, message.getText(), message.getChatId()));
    }
    @Override
    public void LoggerError(Update update) {
        String massage = "Понять почему произошла ошибка и обработать лог: %s";
        log.error(String.format(massage, update));
    }
    @Override
    public void LoggerErrorWrongAppUserRole(Update update) {
        String message = "tgId %s | tgName %s | messageWrongAppUserRole: %s";
        log.error(String.format(message,
                update.getMessage().getFrom().getId(),
                update.getMessage().getFrom().getUserName(),
                update.getMessage().getText()));
    }
    @Override
    public void LoggerErrorWrongEmail(Update update) {
        String message = "tgId %s | tgName %s | messageWrongEmail: %s";
        log.error(String.format(message,
                update.getMessage().getFrom().getId(),
                update.getMessage().getFrom().getUserName(),
                update.getMessage().getText()));
    }
    @Override
    public void LoggerErrorAlreadyRegisteredEmail(Update update) {
        String message = "tgId %s | tgName %s | messageAlreadyRegisteredEmail: %s";
        log.error(String.format(message,
                update.getMessage().getFrom().getId(),
                update.getMessage().getFrom().getUserName(),
                update.getMessage().getText()));
    }
    @Override
    public void LoggerErrorInvalidDepartmentTelegramUserId(Update update) {
        String message = "tgId %s | tgName %s | messageInvalidDepartmentTelegramUserId: %s";
        log.error(String.format(message,
                update.getMessage().getFrom().getId(),
                update.getMessage().getFrom().getUserName(),
                update.getMessage().getText()));
    }
    @Override
    public void LoggerErrorAlreadyRegisteredTelegramUserId(Update update) {
        String message = "tgId %s | tgName %s | messageErrorAlreadyRegisteredTelegramUserId: %s";
        log.error(String.format(message,
                update.getMessage().getFrom().getId(),
                update.getMessage().getFrom().getUserName(),
                update.getMessage().getText()));
    }
    @Override
    public void LoggerErrorUnsupportedMessageType(Update update) {
        log.info("Unsupported received message type: " + update);
    }
}

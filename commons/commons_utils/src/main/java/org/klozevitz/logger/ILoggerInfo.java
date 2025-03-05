package org.klozevitz.logger;


import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface ILoggerInfo {
    void LoggerInfoUpdate(Update update);
    void LoggerInfoMessage(SendMessage message);
    void LoggerError(Update update);
    void LoggerErrorWrongAppUserRole(Update update);
    void LoggerErrorWrongEmail(Update update);
    void LoggerErrorAlreadyRegisteredEmail(Update update);
    void LoggerErrorInvalidDepartmentTelegramUserId(Update update);
    void LoggerErrorAlreadyRegisteredTelegramUserId(Update update);
    void LoggerErrorUnsupportedMessageType(Update update);
}

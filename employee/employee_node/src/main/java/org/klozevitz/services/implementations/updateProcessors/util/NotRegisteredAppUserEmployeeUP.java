package org.klozevitz.services.implementations.updateProcessors.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.services.messageProcessors.WrongAppUserDataUpdateProcessor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Log4j
@RequiredArgsConstructor
public class NotRegisteredAppUserEmployeeUP implements WrongAppUserDataUpdateProcessor {
    @Override
    public SendMessage processUpdate(Update update) {
        return null;
    }
    //    private final EmployeeTelegramView telegramView;

}

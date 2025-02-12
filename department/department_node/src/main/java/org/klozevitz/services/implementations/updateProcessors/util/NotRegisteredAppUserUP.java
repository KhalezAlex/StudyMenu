package org.klozevitz.services.implementations.updateProcessors.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.klozevitz.DepartmentTelegramView;
import org.klozevitz.services.messageProcessors.WrongAppUserDataUpdateProcessor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Log
@RequiredArgsConstructor
public class NotRegisteredAppUserUP implements WrongAppUserDataUpdateProcessor {
    private final DepartmentTelegramView telegramView;

    @Override
    public SendMessage processUpdate(Update update) {
        return telegramView.notRegisteredDepartmentErrorView(update);
    }
}

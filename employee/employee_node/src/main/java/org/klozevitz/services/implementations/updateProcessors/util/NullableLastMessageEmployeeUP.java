package org.klozevitz.services.implementations.updateProcessors.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.EmployeeTelegramView;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.services.interfaces.updateProcessors.UpdateProcessor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Log4j
@RequiredArgsConstructor
public class NullableLastMessageEmployeeUP implements UpdateProcessor {
    private final AppUserRepo appUserRepo;
    private final EmployeeTelegramView telegramView;

    @Override
    public SendMessage processUpdate(Update update) {
        var telegramUserId = telegramUserId(update);
        return null;
    }
}

package org.klozevitz.services.implementations.updateProcessors_LEGACY.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.DepartmentTelegramView;
import org.klozevitz.services.messageProcessors.WrongAppUserDataUpdateProcessor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Log4j
@RequiredArgsConstructor
public class WrongAppUserRoleDepartmentUP_LEGACY implements WrongAppUserDataUpdateProcessor {
    private final DepartmentTelegramView telegramView;
    @Override
    public SendMessage processUpdate(Update update) {
        return telegramView.wrongAppUserRoleErrorView(update);
    }
}

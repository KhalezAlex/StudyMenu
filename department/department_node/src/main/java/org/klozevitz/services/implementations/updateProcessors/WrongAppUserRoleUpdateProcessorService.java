package org.klozevitz.services.implementations.updateProcessors;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.DepartmentTelegramView;
import org.klozevitz.messageProcessors.WrongAppUserRoleUpdateProcessor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Log4j
@Service
@RequiredArgsConstructor
public class WrongAppUserRoleUpdateProcessorService implements WrongAppUserRoleUpdateProcessor {
    private final DepartmentTelegramView telegramView;
    @Override
    public SendMessage processUpdate(Update update) {
        return telegramView.wrongAppUserRoleErrorView(update);
    }
}

package org.klozevitz.services.implementations.updateProcessors;

import lombok.RequiredArgsConstructor;
import org.klozevitz.CompanyTelegramView;
import org.klozevitz.messageProcessors.legacy.utils.WrongAppUserRoleUpdateProcessor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@RequiredArgsConstructor
public class WrongAppUserRoleUpdateProcessorService implements WrongAppUserRoleUpdateProcessor {
    private final CompanyTelegramView telegramView;

    @Override
    public SendMessage processUpdate(Update update) {
        return telegramView.wrongAppUserRoleErrorView(update);
    }
}

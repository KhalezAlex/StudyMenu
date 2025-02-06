package org.klozevitz.services.implementations.updateProcessors;

import lombok.RequiredArgsConstructor;
import org.klozevitz.TelegramView;
import org.klozevitz.services.interfaces.updateProcessors.WrongAppUserRoleUpdateProcessor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@RequiredArgsConstructor
public class WrongAppUserRoleUpdateProcessorService implements WrongAppUserRoleUpdateProcessor {
    private final TelegramView telegramView;

    @Override
    public SendMessage processUpdate(Update update) {
        return telegramView.wrongAppUserRoleErrorView(update);
    }
}

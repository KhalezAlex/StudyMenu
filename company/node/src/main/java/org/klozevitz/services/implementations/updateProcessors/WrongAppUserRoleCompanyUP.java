package org.klozevitz.services.implementations.updateProcessors;

import lombok.RequiredArgsConstructor;
import org.klozevitz.CompanyTelegramView;
import org.klozevitz.services.messageProcessors.WrongAppUserDataUpdateProcessor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
public class WrongAppUserRoleCompanyUP implements WrongAppUserDataUpdateProcessor {
    private final CompanyTelegramView telegramView;

    @Override
    public SendMessage processUpdate(Update update) {
        return telegramView.wrongAppUserRoleErrorView(update);
    }
}

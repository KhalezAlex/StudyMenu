package org.klozevitz.services.implementations.updateProcessors.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.CompanyTelegramView;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.services.messageProcessors.UpdateProcessor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Log4j
@RequiredArgsConstructor
public class PreviousViewCompanyUpdateProcessor implements UpdateProcessor {
    private final CompanyTelegramView telegramView;

    @Override
    public SendMessage processUpdate(Update update, AppUser currentAppUser) {
        return telegramView.previousView(update, currentAppUser);
    }
}

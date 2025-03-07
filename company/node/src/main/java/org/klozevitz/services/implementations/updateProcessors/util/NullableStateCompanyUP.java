package org.klozevitz.services.implementations.updateProcessors.util;

import lombok.RequiredArgsConstructor;
import org.klozevitz.CompanyTelegramView;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.logger.LoggerInfo;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.services.messageProcessors.UpdateProcessor_LEGACY;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.klozevitz.enitites.appUsers.enums.views.CompanyView.NULL_COMPANY_STATE_NOTIFICATION_VIEW;

@RequiredArgsConstructor
public class NullableStateCompanyUP implements UpdateProcessor_LEGACY {
    private final AppUserRepo appUserRepo;
    private final CompanyTelegramView telegramView;
    private final LoggerInfo loggerInfo;

    @Override
    public SendMessage processUpdate(Update update, AppUser currentAppUser) {
        currentAppUser.getCompany().setCurrentView(NULL_COMPANY_STATE_NOTIFICATION_VIEW);
        appUserRepo.save(currentAppUser);
        loggerInfo.LoggerError(update);

        return telegramView.nullCompanyStateNotificationView(update);
    }
}

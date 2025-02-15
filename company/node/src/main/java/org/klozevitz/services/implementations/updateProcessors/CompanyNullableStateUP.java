package org.klozevitz.services.implementations.updateProcessors;

import lombok.RequiredArgsConstructor;
import org.klozevitz.CompanyTelegramView;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.services.messageProcessors.UpdateProcessor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.klozevitz.enitites.appUsers.enums.views.CompanyView.NULL_COMPANY_STATE_NOTIFICATION_VIEW;

@RequiredArgsConstructor
public class CompanyNullableStateUP implements UpdateProcessor {
    private final AppUserRepo appUserRepo;
    private final CompanyTelegramView telegramView;

    @Override
    public SendMessage processUpdate(Update update, AppUser currentAppUser) {
        currentAppUser.getCompany().setCurrentView(NULL_COMPANY_STATE_NOTIFICATION_VIEW);
        appUserRepo.save(currentAppUser);

        return telegramView.nullCompanyStateNotificationView(update);
    }
}

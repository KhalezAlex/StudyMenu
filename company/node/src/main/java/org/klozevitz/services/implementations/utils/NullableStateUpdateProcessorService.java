package org.klozevitz.services.implementations.utils;

import lombok.RequiredArgsConstructor;
import org.klozevitz.CompanyTelegramView;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.messageProcessors.utils.NullableStateUpdateProcessor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.klozevitz.enitites.appUsers.enums.views.CompanyView.NULL_COMPANY_STATE_NOTIFICATION_VIEW;


@Service
@RequiredArgsConstructor
public class NullableStateUpdateProcessorService implements NullableStateUpdateProcessor {
    private final CompanyTelegramView telegramView;
    private final AppUserRepo appUserRepo;

    @Override
    public SendMessage processUpdate(Update update, AppUser currentAppUser) {
        currentAppUser.getCompany().setCurrentView(NULL_COMPANY_STATE_NOTIFICATION_VIEW);
        appUserRepo.save(currentAppUser);

        return telegramView.nullCompanyStateNotificationView(update);
    }
}

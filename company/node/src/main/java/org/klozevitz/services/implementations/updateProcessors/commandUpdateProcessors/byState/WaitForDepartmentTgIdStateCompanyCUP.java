package org.klozevitz.services.implementations.updateProcessors.commandUpdateProcessors.byState;

import lombok.RequiredArgsConstructor;
import org.klozevitz.CompanyTelegramView;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.services.messageProcessors.UpdateProcessor_LEGACY;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.klozevitz.enitites.appUsers.enums.views.CompanyView.WELCOME_VIEW;

@RequiredArgsConstructor
public class WaitForDepartmentTgIdStateCompanyCUP implements UpdateProcessor_LEGACY {
    private final AppUserRepo appUserRepo;
    private final CompanyTelegramView telegramView;
    private final UpdateProcessor_LEGACY previousViewUpdateProcessor;

    @Override
    public SendMessage processUpdate(Update update, AppUser currentAppUser) {
        var command = update.getMessage().getText();

        switch (command) {
            case "/start":
                return registeredWelcomeView(update, currentAppUser);
            default:
                return previousViewUpdateProcessor.processUpdate(update, currentAppUser);
        }
    }

    private SendMessage registeredWelcomeView(Update update, AppUser currentAppUser) {
        currentAppUser.getCompany().setCurrentView(WELCOME_VIEW);
        appUserRepo.save(currentAppUser);

        return telegramView.registeredWelcomeView(update);
    }
}

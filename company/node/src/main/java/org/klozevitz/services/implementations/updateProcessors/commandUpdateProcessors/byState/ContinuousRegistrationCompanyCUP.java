package org.klozevitz.services.implementations.updateProcessors.commandUpdateProcessors.byState;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.CompanyTelegramView;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.services.messageProcessors.UpdateProcessor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.klozevitz.enitites.appUsers.enums.states.CompanyState.WAIT_FOR_EMAIL_CONFIRMATION_STATE;
import static org.klozevitz.enitites.appUsers.enums.states.CompanyState.WAIT_FOR_EMAIL_STATE;
import static org.klozevitz.enitites.appUsers.enums.views.CompanyView.EMAIL_CONFIRMATION_REQUEST_VIEW;
import static org.klozevitz.enitites.appUsers.enums.views.CompanyView.EMAIL_REQUEST_VIEW;

@Log4j
@RequiredArgsConstructor
public class ContinuousRegistrationCompanyCUP implements UpdateProcessor {
    private final AppUserRepo appUserRepo;
    private final CompanyTelegramView telegramView;
    private final UpdateProcessor previousViewUpdateProcessor;


    @Override
    public SendMessage processUpdate(Update update, AppUser currentAppUser) {
        var state = currentAppUser.getCompany().getState();

        switch (state) {
            case WAIT_FOR_EMAIL_STATE:
                return emailRequestView(update, currentAppUser);
            case WAIT_FOR_EMAIL_CONFIRMATION_STATE:
                return emailConfirmationRequestView(update, currentAppUser);
            default:
                return previousViewUpdateProcessor.processUpdate(update, currentAppUser);
        }
    }

    private SendMessage emailRequestView(Update update, AppUser currentAppUser) {
        currentAppUser.getCompany().setState(WAIT_FOR_EMAIL_STATE);
        currentAppUser.getCompany().setCurrentView(EMAIL_REQUEST_VIEW);
        appUserRepo.save(currentAppUser);

        return telegramView.emailRequestView(update);
    }

    private SendMessage emailConfirmationRequestView(Update update, AppUser currentAppUser) {
        currentAppUser.getCompany().setState(WAIT_FOR_EMAIL_CONFIRMATION_STATE);
        currentAppUser.getCompany().setCurrentView(EMAIL_CONFIRMATION_REQUEST_VIEW);
        appUserRepo.save(currentAppUser);

        return telegramView.emailConfirmationRequestView(update);
    }
}

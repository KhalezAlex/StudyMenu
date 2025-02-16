package org.klozevitz.services.implementations.updateProcessors.commandUpdateProcessors;

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
import static org.klozevitz.enitites.appUsers.enums.views.CompanyView.*;

@Log4j
@RequiredArgsConstructor
public class CommandCompanyUP implements UpdateProcessor {
    private final AppUserRepo appUserRepo;
    private final CompanyTelegramView telegramView;
    private final UpdateProcessor nullableStateUpdateProcessor;
    private final UpdateProcessor basicStateCUP;
    private final UpdateProcessor unregisteredStateCUP;
    private final UpdateProcessor waitForDepartmentTelegramUserIdStateCUP;
    private final UpdateProcessor previousViewUpdateProcessor;

    @Override
    public SendMessage processUpdate(Update update, AppUser currentAppUser) {
        var state = currentAppUser.getCompany().getState();

        if (state == null) {
            return nullableStateUpdateProcessor.processUpdate(update, currentAppUser);
        }

        switch (state) {
            case UNREGISTERED_STATE:
                return unregisteredStateCUP.processUpdate(update, currentAppUser);
            case WAIT_FOR_EMAIL_STATE:
                return emailRequestView(update, currentAppUser);
            case WAIT_FOR_EMAIL_CONFIRMATION_STATE:
                return emailConfirmationRequestView(update, currentAppUser);
            case BASIC_STATE:
                return basicStateCUP.processUpdate(update, currentAppUser);
            case WAIT_FOR_DEPARTMENT_TELEGRAM_USER_ID_STATE:
                return waitForDepartmentTelegramUserIdStateCUP.processUpdate(update, currentAppUser);
            default: {
                log.error("Сообщение не попало ни в одну из веток состояний компании");
                return previousViewUpdateProcessor.processUpdate(update, currentAppUser);
            }
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

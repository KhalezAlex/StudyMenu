package org.klozevitz.services.implementations.messageProcessors.commandMessageProcessors;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.TelegramView;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.messageProcessors.CommandMessageProcessor;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.klozevitz.enitites.appUsers.enums.states.CompanyState.WAITING_FOR_EMAIL_CONFIRMATION_STATE;
import static org.klozevitz.enitites.appUsers.enums.states.CompanyState.WAITING_FOR_EMAIL_STATE;
import static org.klozevitz.enitites.appUsers.enums.views.CompanyView.*;

@Log4j
@Service
@RequiredArgsConstructor
public class CommandMessageProcessorService implements CommandMessageProcessor {
    private final String NULL_COMPANY_STATE_ERROR_MESSAGE = "Пользователь id=%d не имеет состояния после вью %s";

    private final TelegramView telegramView;
    private final AppUserRepo appUserRepo;

    /**
     * TODO: default- временный
     * */
    @Override
    public SendMessage processCommandMessage(Update update, AppUser currentAppUser) {
        var state = currentAppUser.getCompany().getState();

        if (state == null) {
            return nullCompanyStateNotificationMessage(update, currentAppUser);
        }

        switch (state) {
            case UNREGISTERED_STATE:
                return unregisteredWelcomeView(update, currentAppUser);
            case WAITING_FOR_EMAIL_STATE:
                return emailRequestView(update, currentAppUser);
            case WAITING_FOR_EMAIL_CONFIRMATION_STATE:
                return emailConfirmationRequestView(update, currentAppUser);
            case BASIC_STATE:
                return basicStateViewStrategy(update, currentAppUser);
            default: {
                log.error("Сообщение не попало ни в одну из веток состояний компании");
                return telegramView.previousView(update, currentAppUser);
            }
        }
    }

    private SendMessage nullCompanyStateNotificationMessage(Update update, AppUser currentAppUser) {
        var telegramUserId = currentAppUser.getTelegramUserId();
        var currentView = currentAppUser.getCompany().getCurrentView();
        var error = String.format(NULL_COMPANY_STATE_ERROR_MESSAGE, telegramUserId, currentView);
        log.error(error);
        return telegramView.nullCompanyStateNotificationMessage(update);
    }

    private SendMessage unregisteredWelcomeView(Update update, AppUser currentAppUser) {
        currentAppUser.getCompany().setCurrentView(UNREGISTERED_WELCOME_VIEW);
        return telegramView.unregisteredWelcomeView(update);
    }

    private SendMessage emailRequestView(Update update, AppUser currentAppUser) {
        currentAppUser.getCompany().setState(WAITING_FOR_EMAIL_STATE);
        currentAppUser.getCompany().setCurrentView(EMAIL_REQUEST_VIEW);
        appUserRepo.save(currentAppUser);

        return telegramView.emailRequestView(update);
    }

    private SendMessage emailConfirmationRequestView(Update update, AppUser currentAppUser) {
        currentAppUser.getCompany().setState(WAITING_FOR_EMAIL_CONFIRMATION_STATE);
        currentAppUser.getCompany().setCurrentView(EMAIL_CONFIRMATION_REQUEST_VIEW);
        appUserRepo.save(currentAppUser);

        return telegramView.emailConfirmationRequestView(update);
    }

    private SendMessage basicStateViewStrategy(Update update, AppUser currentAppUser) {
        var command = update.getMessage().getText();

        switch (command) {
            case "/start":
                return registeredWelcomeView(update, currentAppUser);
        }
        return null;
    }

    private SendMessage registeredWelcomeView(Update update, AppUser currentAppUser) {
        currentAppUser.getCompany().setCurrentView(REGISTERED_WELCOME_VIEW);
        appUserRepo.save(currentAppUser);

        return telegramView.registeredWelcomeView(update);
    }
}

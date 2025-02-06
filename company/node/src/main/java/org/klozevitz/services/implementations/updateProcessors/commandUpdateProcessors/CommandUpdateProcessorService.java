package org.klozevitz.services.implementations.updateProcessors.commandUpdateProcessors;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.TelegramView;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.messageProcessors.CommandUpdateProcessor;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.services.interfaces.updateProcessors.NullableStateUpdateProcessor;
import org.klozevitz.services.interfaces.updateProcessors.WrongAppUserRoleUpdateProcessor;
import org.klozevitz.services.interfaces.updateProcessors.commandUpdateProcessors.BasicStateCUP;
import org.klozevitz.services.interfaces.updateProcessors.commandUpdateProcessors.UnregisteredStateCUP;
import org.klozevitz.services.interfaces.updateProcessors.commandUpdateProcessors.WaitingForDepartmentTelegramUserIdStateCUP;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.klozevitz.enitites.appUsers.enums.states.CompanyState.WAITING_FOR_EMAIL_CONFIRMATION_STATE;
import static org.klozevitz.enitites.appUsers.enums.states.CompanyState.WAITING_FOR_EMAIL_STATE;
import static org.klozevitz.enitites.appUsers.enums.views.CompanyView.*;

@Log4j
@Service("commandUpdateProcessor")
@RequiredArgsConstructor
public class CommandUpdateProcessorService implements CommandUpdateProcessor {
    private final TelegramView telegramView;
    private final AppUserRepo appUserRepo;
    private final NullableStateUpdateProcessor nullableStateUpdateProcessor;
    private final BasicStateCUP basicStateCUP;
    private final UnregisteredStateCUP unregisteredStateCUP;
    private final WaitingForDepartmentTelegramUserIdStateCUP waitingForDepartmentTelegramUserIdStateCUP;
    private final WrongAppUserRoleUpdateProcessor wrongAppUserRoleUpdateProcessor;

    @Override
    public SendMessage processCommandMessage(Update update, AppUser currentAppUser) {

        var company = currentAppUser.getCompany();

        if (company == null) {
            return wrongAppUserRoleUpdateProcessor.processUpdate(update);
        }

        var state = currentAppUser.getCompany().getState();

        if (state == null) {
            return nullableStateUpdateProcessor.processUpdate(update, currentAppUser);
        }

        switch (state) {
            case UNREGISTERED_STATE:
                return unregisteredStateCUP.processCommandMessage(update, currentAppUser);
            case WAITING_FOR_EMAIL_STATE:
                return emailRequestView(update, currentAppUser);
            case WAITING_FOR_EMAIL_CONFIRMATION_STATE:
                return emailConfirmationRequestView(update, currentAppUser);
            case BASIC_STATE:
                return basicStateCUP.processCommandMessage(update, currentAppUser);
            case WAITING_FOR_DEPARTMENT_TELEGRAM_USER_ID_STATE:
                return waitingForDepartmentTelegramUserIdStateCUP.processCommandMessage(update, currentAppUser);
            default: {
                log.error("Сообщение не попало ни в одну из веток состояний компании");
                return telegramView.previousView(update, currentAppUser);
            }
        }
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
}

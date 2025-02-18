package org.klozevitz.services.implementations.updateProcessors.commandUpdateProcessors;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.services.messageProcessors.UpdateProcessor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Log4j
@RequiredArgsConstructor
public class CommandCompanyUP implements UpdateProcessor {
    private final UpdateProcessor nullableStateUpdateProcessor;
    private final UpdateProcessor basicStateCUP;
    private final UpdateProcessor unregisteredStateCUP;
    private final UpdateProcessor waitForDepartmentTgIdStateCUP;
    private final UpdateProcessor continuousRegistrationCUP;
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
            case WAIT_FOR_EMAIL_CONFIRMATION_STATE:
                return continuousRegistrationCUP.processUpdate(update, currentAppUser);
            case BASIC_STATE:
                return basicStateCUP.processUpdate(update, currentAppUser);
            case WAIT_FOR_DEPARTMENT_TG_ID_STATE:
                return waitForDepartmentTgIdStateCUP.processUpdate(update, currentAppUser);
            default: {
                log.error("Сообщение не попало ни в одну из веток состояний компании");
                return previousViewUpdateProcessor.processUpdate(update, currentAppUser);
            }
        }
    }


}

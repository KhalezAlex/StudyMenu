package org.klozevitz.services.implementations.updateProcessors.callbackQueryUpdateProcessors;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.services.messageProcessors.UpdateProcessor_LEGACY;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Log4j
@RequiredArgsConstructor
public class CallbackQueryCompanyUP implements UpdateProcessor_LEGACY {
    private final UpdateProcessor_LEGACY nullableStateUpdateProcessor;
    private final UpdateProcessor_LEGACY unregisteredStateCQUP;
    private final UpdateProcessor_LEGACY waitingForEmailStateCQUP;
    private final UpdateProcessor_LEGACY basicStateCQUP;
    private final UpdateProcessor_LEGACY waitingForDepartmentTgIdStateCQUP;
    private final UpdateProcessor_LEGACY previousViewUpdateProcessor;

    @Override
    public SendMessage processUpdate(Update update, AppUser currentAppUser) {
        var state = currentAppUser.getCompany().getState();

        if (state == null) {
            return nullableStateUpdateProcessor.processUpdate(update, currentAppUser);
        }

        switch (state) {
            case UNREGISTERED_STATE:
                return unregisteredStateCQUP.processUpdate(update, currentAppUser);
            case WAIT_FOR_EMAIL_CONFIRMATION_STATE:
                // TODO перевести этот вызов на соответстующий сервис
                return waitingForEmailStateCQUP.processUpdate(update, currentAppUser);
            case BASIC_STATE:
                return basicStateCQUP.processUpdate(update, currentAppUser);
            case WAIT_FOR_DEPARTMENT_TG_ID_STATE:
                return waitingForDepartmentTgIdStateCQUP.processUpdate(update, currentAppUser);
            default: {
                log.error("Сообщение не попало ни в одну из веток состояний компании");
                return previousViewUpdateProcessor.processUpdate(update, currentAppUser);
            }
        }
    }


}

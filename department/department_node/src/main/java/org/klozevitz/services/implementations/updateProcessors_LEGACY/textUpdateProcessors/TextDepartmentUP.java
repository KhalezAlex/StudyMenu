package org.klozevitz.services.implementations.updateProcessors_LEGACY.textUpdateProcessors;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.services.messageProcessors.UpdateProcessor_LEGACY;
import org.klozevitz.enitites.appUsers.AppUser;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Log4j
@RequiredArgsConstructor
public class TextDepartmentUP implements UpdateProcessor_LEGACY {
    private final UpdateProcessor_LEGACY nullableStateUpdateProcessor;
    private final UpdateProcessor_LEGACY previousViewUpdateProcessor;
    private final UpdateProcessor_LEGACY waitForEmployeeTgIdStateTextUpdateProcessor;

    @Override
    public SendMessage processUpdate(Update update, AppUser currentAppUser) {
        var state = currentAppUser.getDepartment().getState();

        if (state == null) {
            return nullableStateUpdateProcessor.processUpdate(update, currentAppUser);
        }

        switch (state) {
            case WAIT_FOR_EMPLOYEE_TG_ID_STATE:
                return waitForEmployeeTgIdStateTextUpdateProcessor.processUpdate(update, currentAppUser);
            default:
                return previousViewUpdateProcessor.processUpdate(update, currentAppUser);
        }
    }
}

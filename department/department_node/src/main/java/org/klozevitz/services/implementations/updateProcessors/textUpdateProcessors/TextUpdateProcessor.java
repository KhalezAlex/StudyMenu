package org.klozevitz.services.implementations.updateProcessors.textUpdateProcessors;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.services.messageProcessors.UpdateProcessor;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.services.uitl.Registrar;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Log4j
@RequiredArgsConstructor
public class TextUpdateProcessor implements UpdateProcessor {

    private final UpdateProcessor nullableStateUpdateProcessor;
    private final Registrar employeeRegistrar;

    @Override
    public SendMessage processUpdate(Update update, AppUser currentAppUser) {
        var state = currentAppUser.getDepartment().getState();

        if (state == null) {
            nullableStateUpdateProcessor.processUpdate(update, currentAppUser);
        }

        switch (state) {
            case WAIT_FOR_EMPLOYEE_TG_ID_STATE:
                return employeeRegistrar.register(update, currentAppUser);
        }
        return null;
    }
}

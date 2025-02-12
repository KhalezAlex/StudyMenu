package org.klozevitz.services.implementations.updateProcessors.textUpdateProcessors;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.services.messageProcessors.UpdateProcessor;
import org.klozevitz.enitites.appUsers.AppUser;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.annotation.Resource;

@Log4j
@RequiredArgsConstructor
public class TextUP implements UpdateProcessor {
    @Resource(name = "nullableState_UpdateProcessor")
    private final UpdateProcessor nullableStateUpdateProcessor;
    @Resource(name = "previousView_UpdateProcessor")
    private UpdateProcessor previousViewUpdateProcessor;
    @Resource(name = "waitForEmployeeTgIdState_Text_UpdateProcessor")
    private UpdateProcessor waitForEmployeeTgIdStateTextUpdateProcessor;

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

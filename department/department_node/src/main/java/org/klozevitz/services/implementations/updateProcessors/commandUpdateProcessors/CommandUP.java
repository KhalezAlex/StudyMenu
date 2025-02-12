package org.klozevitz.services.implementations.updateProcessors.commandUpdateProcessors;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.services.messageProcessors.UpdateProcessor;
import org.klozevitz.enitites.appUsers.AppUser;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.annotation.Resource;

@Log4j
@RequiredArgsConstructor
public class CommandUP implements UpdateProcessor {
    @Resource(name = "nullableState_UpdateProcessor")
    private UpdateProcessor nullableStateUpdateProcessor;
    @Resource(name = "previousView_UpdateProcessor")
    private UpdateProcessor previousViewUpdateProcessor;
    @Resource(name = "basicStateCommand_UpdateProcessor")
    private UpdateProcessor basicStateCommandUpdateProcessor;


    @Override
    public SendMessage processUpdate(Update update, AppUser currentAppUser) {
        var state = currentAppUser.getDepartment().getState();

        if (state == null) {
            return nullableStateUpdateProcessor.processUpdate(update, currentAppUser);
        }

        switch (state) {
            case BASIC_STATE:
                return basicStateCommandUpdateProcessor.processUpdate(update, currentAppUser);
            default:
                return previousViewUpdateProcessor.processUpdate(update, currentAppUser);
        }
    }
}

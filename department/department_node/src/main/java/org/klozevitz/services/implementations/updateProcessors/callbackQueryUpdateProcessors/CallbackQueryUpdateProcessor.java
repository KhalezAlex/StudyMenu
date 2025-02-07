package org.klozevitz.services.implementations.updateProcessors.callbackQueryUpdateProcessors;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.UpdateProcessor;
import org.klozevitz.enitites.appUsers.AppUser;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.annotation.Resource;

@Log4j
@RequiredArgsConstructor
public class CallbackQueryUpdateProcessor implements UpdateProcessor {
    @Resource(name = "nullableStateUpdateProcessor")
    private UpdateProcessor nullableStateUpdateProcessor;
    @Resource(name = "basicStateCallbackQueryUpdateProcessor")
    private UpdateProcessor basicStateCallbackQueryUpdateProcessor;

    @Override
    public SendMessage processUpdate(Update update, AppUser currentAppUser) {
        var state = currentAppUser.getDepartment().getState();

        if (state == null) {
            return nullableStateUpdateProcessor.processUpdate(update, currentAppUser);
        }

        switch (state) {
            case BASIC_STATE:
                return basicStateCallbackQueryUpdateProcessor.processUpdate(update, currentAppUser);
        }
        return null;
    }
}

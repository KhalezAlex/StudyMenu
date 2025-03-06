package org.klozevitz.services.implementations.updateProcessors_LEGACY.textUpdateProcessors;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.services.interfaces.updateProcessors.UpdateProcessor_LEGACY;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Log4j
@RequiredArgsConstructor
public class TextEmployeeUP implements UpdateProcessor_LEGACY<Update, AppUser> {
    @Override
    public SendMessage processUpdate(Update update, AppUser currentAppUser) {
        return null;
    }
}

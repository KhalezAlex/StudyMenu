package org.klozevitz.services.implementations.updateProcessors.textUpdateProcessors.byState;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.services.messageProcessors.UpdateProcessor;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.services.util.Registrar;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Log4j
@RequiredArgsConstructor
public class WaitForEmployeeTgIdStateDepartmentTUP implements UpdateProcessor {
    private final Registrar employeeRegistrar;

    @Override
    public SendMessage processUpdate(Update update, AppUser currentAppUser) {
        return employeeRegistrar.register(update, currentAppUser);
    }
}

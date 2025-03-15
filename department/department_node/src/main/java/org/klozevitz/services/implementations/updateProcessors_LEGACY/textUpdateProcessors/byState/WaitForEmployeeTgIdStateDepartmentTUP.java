package org.klozevitz.services.implementations.updateProcessors_LEGACY.textUpdateProcessors.byState;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.services.messageProcessors.UpdateProcessor_LEGACY;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.services.util.Registrar_LEGACY;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Log4j
@RequiredArgsConstructor
public class WaitForEmployeeTgIdStateDepartmentTUP implements UpdateProcessor_LEGACY {
    private final Registrar_LEGACY employeeRegistrarLEGACY;

    @Override
    public SendMessage processUpdate(Update update, AppUser currentAppUser) {
        return employeeRegistrarLEGACY.register(update, currentAppUser);
    }
}

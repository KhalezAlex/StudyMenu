package org.klozevitz.services.implementations.updateProcessors_LEGACY.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.EmployeeTelegramView;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.services.interfaces.updateProcessors.UpdateProcessor_LEGACY;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Log4j
@RequiredArgsConstructor
public class WrongAppUserRoleEmployeeUP_LEGACY implements UpdateProcessor_LEGACY<Update, AppUser> {
    private final EmployeeTelegramView telegramView;

    @Override
    public SendMessage processUpdate(Update update, AppUser appUser) {
        return telegramView.wrongAppUserRoleErrorView(update);
    }
}

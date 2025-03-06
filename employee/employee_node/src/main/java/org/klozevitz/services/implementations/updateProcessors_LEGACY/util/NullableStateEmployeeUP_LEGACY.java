package org.klozevitz.services.implementations.updateProcessors_LEGACY.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.EmployeeTelegramView;
import org.klozevitz.repositories.appUsers.EmployeeRepo;
import org.klozevitz.services.interfaces.updateProcessors.UpdateProcessor_LEGACY;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.klozevitz.enitites.appUsers.enums.views.EmployeeView.NULL_STATE_ERROR_VIEW;

@Log4j
@RequiredArgsConstructor
public class NullableStateEmployeeUP_LEGACY implements UpdateProcessor_LEGACY<Update, Long> {
    private final EmployeeRepo employeeRepo;
    private final EmployeeTelegramView telegramView;

    @Override
    public SendMessage processUpdate(Update update, Long telegramUserId) {
        employeeRepo.setEmployeeCurrentView(NULL_STATE_ERROR_VIEW.name(), telegramUserId);

        return telegramView.nullStateErrorView(update);
    }
}

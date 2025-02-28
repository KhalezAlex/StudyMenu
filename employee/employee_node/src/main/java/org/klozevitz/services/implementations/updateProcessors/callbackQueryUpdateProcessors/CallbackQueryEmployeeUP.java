package org.klozevitz.services.implementations.updateProcessors.callbackQueryUpdateProcessors;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.enitites.appUsers.enums.states.EmployeeState;
import org.klozevitz.enitites.appUsers.enums.views.EmployeeView;
import org.klozevitz.services.interfaces.updateProcessors.UpdateProcessor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

@Log4j
@RequiredArgsConstructor
public class CallbackQueryEmployeeUP implements UpdateProcessor<Update, AppUser> {
    public final UpdateProcessor<Update, Long> nullableStateUpdateProcessor;
    public final UpdateProcessor<Update, EmployeeView> previousViewUpdateProcessor;
    public final Map<EmployeeState, UpdateProcessor<Update, EmployeeView>> stateDispatcher;

    @Override
    public SendMessage processUpdate(Update update, AppUser currentAppUser) {
        var state = currentAppUser.getEmployee().getState();
        var telegramUserId = currentAppUser.getTelegramUserId();
        var currentView = currentAppUser.getEmployee().getCurrentView();

        if (state == null) {
            return nullableStateUpdateProcessor.processUpdate(update, telegramUserId);
        }

        var stateProcessor = stateDispatcher.get(state);

        if (stateProcessor == null) {
            log.error("Сообщение не попало ни в одну из веток состояний сотрудника");
            return previousViewUpdateProcessor.processUpdate(update, currentView);
        }


        return stateProcessor.processUpdate(update, currentView);
    }
}

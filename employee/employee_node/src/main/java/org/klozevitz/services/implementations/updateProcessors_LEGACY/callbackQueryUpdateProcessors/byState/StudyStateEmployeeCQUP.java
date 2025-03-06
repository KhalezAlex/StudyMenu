package org.klozevitz.services.implementations.updateProcessors_LEGACY.callbackQueryUpdateProcessors.byState;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.enitites.appUsers.enums.views.EmployeeView;
import org.klozevitz.services.interfaces.updateProcessors.UpdateProcessor_LEGACY;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

@Log4j
@RequiredArgsConstructor
public class StudyStateEmployeeCQUP implements UpdateProcessor_LEGACY<Update, EmployeeView> {
    // TODO обязательно сделать свой previousViewUpdateProcessor, потому что стандартный не подходит

    private final UpdateProcessor_LEGACY<Update, EmployeeView> previousViewUpdateProcessor;
    private final Map<String, UpdateProcessor_LEGACY<Update, Long>> commandDispatcher;

    @Override
    public SendMessage processUpdate(Update update, EmployeeView currentView) {
        var command = update.getCallbackQuery().getData();
        var telegramUserId = telegramUserId(update);

        var viewResolver = commandDispatcher.get(command);

        if (viewResolver == null) {
            return previousViewUpdateProcessor.processUpdate(update, currentView);
        }

        return viewResolver.processUpdate(update, telegramUserId);
    }

    private long telegramUserId(Update update) {
        return update
                .getCallbackQuery()
                .getFrom()
                .getId();
    }
}

package org.klozevitz.services.implementations.updateProcessors.callbackQueryUpdateProcessors.byState;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.enitites.appUsers.enums.views.EmployeeView;
import org.klozevitz.services.interfaces.updateProcessors.UpdateProcessor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

@Log4j
@RequiredArgsConstructor
public class BasicStateEmployeeCQUP implements UpdateProcessor<Update, EmployeeView> {
    private final UpdateProcessor<Update, EmployeeView> previousViewUpdateProcessor;
    private final Map<String, UpdateProcessor<Update, Long>> commandDispatcher;

    @Override
    public SendMessage processUpdate(Update update, EmployeeView currentView) {
        var command = update.getCallbackQuery().getData();
        var telegramUserId = telegramUserId(update);

        if (Character.isDigit(command.charAt(command.length() - 1))) {
            command = command.substring(0, command.lastIndexOf("_") + 1);
        }

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

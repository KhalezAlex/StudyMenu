package org.klozevitz.services.implementations.updateProcessors.callbackQueryUpdateProcessors.byState;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.enitites.appUsers.enums.views.EmployeeView;
import org.klozevitz.services.interfaces.updateProcessors.UpdateProcessor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Log4j
@RequiredArgsConstructor
public class StudyStatePreviousViewUP implements UpdateProcessor<Update, EmployeeView> {
    private final UpdateProcessor<Update, Long> resourcesSlideViewResolver;

    @Override
    public SendMessage processUpdate(Update update, EmployeeView employeeView) {
        var telegramUserId = telegramUserId(update);
        return resourcesSlideViewResolver.processUpdate(update, telegramUserId);
    }

    private long telegramUserId(Update update) {
        return update.hasMessage() ?
                update.getMessage().getFrom().getId() :
                update.getCallbackQuery().getFrom().getId();
    }
}

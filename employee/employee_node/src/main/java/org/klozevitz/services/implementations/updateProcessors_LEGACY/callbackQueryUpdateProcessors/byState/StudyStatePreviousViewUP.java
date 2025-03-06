package org.klozevitz.services.implementations.updateProcessors_LEGACY.callbackQueryUpdateProcessors.byState;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.enitites.appUsers.enums.views.EmployeeView;
import org.klozevitz.services.interfaces.updateProcessors.UpdateProcessor_LEGACY;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Log4j
@RequiredArgsConstructor
public class StudyStatePreviousViewUP implements UpdateProcessor_LEGACY<Update, EmployeeView> {
    private final UpdateProcessor_LEGACY<Update, Long> resourcesSlideViewResolver;

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

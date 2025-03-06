package org.klozevitz.services.implementations.updateProcessors.commandProcessors;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.enitites.appUsers.enums.views.EmployeeView;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.services.interfaces.updateProcessors.UpdateProcessor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

@Log4j
@RequiredArgsConstructor
public class CommandEmployeeUP implements UpdateProcessor<Update> {
    private final AppUserRepo appUserRepo;
    private final Map<EmployeeView, UpdateProcessor<Update>> viewDispatcher;
    private final UpdateProcessor<Update> notRegisteredAppUserUpdateProcessor;
    private final UpdateProcessor<Update> previousViewUpdateProcessor;

    @Override
    public SendMessage processUpdate(Update update) {
        var telegramUserId = telegramUserId(update);
        var currentAppUser = appUserRepo.findByTelegramUserId(telegramUserId);

        if (currentAppUser.isEmpty()) {
            log.error("Пользователь не зарегистрирован: " + telegramUserId);
            return notRegisteredAppUserUpdateProcessor.processUpdate(update);
        }

        var currentView = currentAppUser.get().getEmployee().getCurrentView();
        var viewProcessor = viewDispatcher.get(currentView);

        if (viewProcessor == null) {
            log.error("Сообщение не попало ни в один из вью-процессоров");

            return previousViewUpdateProcessor.processUpdate(update);
        }

        return viewProcessor.processUpdate(update);
    }

    private long telegramUserId(Update update) {
        return update.hasMessage() ?
                update.getMessage().getFrom().getId() :
                update.getCallbackQuery().getFrom().getId();
    }
}

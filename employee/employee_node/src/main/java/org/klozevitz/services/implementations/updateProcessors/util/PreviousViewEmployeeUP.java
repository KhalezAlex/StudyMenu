package org.klozevitz.services.implementations.updateProcessors.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.EmployeeTelegramView;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.enitites.appUsers.MessageSent;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.services.interfaces.updateProcessors.UpdateProcessor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.Comparator;

@Log4j
@RequiredArgsConstructor
public class PreviousViewEmployeeUP implements UpdateProcessor {
    private final String PREVIOUS_VIEW_ERROR_MESSAGE = "<b>Вы совершили действие, которое привело к остановке " +
            "выполнения процесса. Вернемся к предыдущему экрану:</b>";
    private final AppUserRepo appUserRepo;
    private final EmployeeTelegramView telegramView;

    @Override
    public SendMessage processUpdate(Update update) {
        var tgUserId = telegramUserId(update);
        var currentAppUser = appUserRepo.findByTelegramUserId(tgUserId);

        if (currentAppUser.isEmpty()) {
            return telegramView.notRegisteredErrorView(update);
        }

        var previousView = previousView(currentAppUser.get());

        return previousView.getText().contains(PREVIOUS_VIEW_ERROR_MESSAGE) ?
                previousView :
                telegramView.addServiceMessage(previousView, PREVIOUS_VIEW_ERROR_MESSAGE);
    }

    private long telegramUserId(Update update) {
        return update.hasMessage() ?
                update.getMessage().getFrom().getId() :
                update.getCallbackQuery().getFrom().getId();
    }

    private SendMessage previousView(AppUser currentAppUser) {
        var messages = new ArrayList<>(currentAppUser.getMessages());

        messages.sort(Comparator.comparingInt(MessageSent::getMessageId));

        var message = messages.get(messages.size() - 1);

        return message.getAnswer();
    }
}

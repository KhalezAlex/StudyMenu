package org.klozevitz.services.implementations.updateProcessors.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.EmployeeTelegramView;
import org.klozevitz.enitites.appUsers.MessageSent;
import org.klozevitz.enitites.appUsers.enums.views.EmployeeView;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.services.interfaces.updateProcessors.UpdateProcessor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.Comparator;

import static org.klozevitz.enitites.appUsers.enums.views.EmployeeView.WELCOME_VIEW;

@Log4j
@RequiredArgsConstructor
public class PreviousViewEmployeeUP implements UpdateProcessor {
    private final String PREVIOUS_VIEW_ERROR_MESSAGE = "<b>Вы совершили действие, которое привело к остановке " +
            "выполнения процесса. Вернемся к предыдущему экрану:</b>";
    private final AppUserRepo appUserRepo;
    private final EmployeeTelegramView telegramView;

    @Override
    public ArrayList<SendMessage> processUpdate(Update update) {
        var tgUserId = telegramUserId(update);
        var currentAppUser = appUserRepo.findByTelegramUserId(tgUserId);
        ArrayList<SendMessage> answer = new ArrayList<>();

        if (currentAppUser.isEmpty()) {
            answer.add(
                    telegramView.notRegisteredErrorView(update)
            );

            return answer;
        }

        var previousView = previousView(update);

        previousView = previousView.getText().contains(PREVIOUS_VIEW_ERROR_MESSAGE) ?
                previousView :
                telegramView.addServiceMessage(previousView, PREVIOUS_VIEW_ERROR_MESSAGE);

        answer.add(previousView);

        return answer;
    }

    // TODO: возможно, придется передавать полный список, чтобы не бесить людей

    private SendMessage previousView(Update update) {
        var telegramUserId = telegramUserId(update);
        var currentAppUser = appUserRepo.findByTelegramUserId(telegramUserId).get();
        var messages = new ArrayList<>(currentAppUser.getMessages());
        var currentView = currentAppUser.getEmployee().getCurrentView();

        if (messages.isEmpty()) {
            currentAppUser.getEmployee().setCurrentView(WELCOME_VIEW);
            appUserRepo.save(currentAppUser);
            return telegramView.previousView(update, currentView);
        }

        messages.sort(Comparator.comparingInt(MessageSent::getMessageId));

        var message = messages.get(messages.size() - 1);
        return message.getAnswer();
    }
}

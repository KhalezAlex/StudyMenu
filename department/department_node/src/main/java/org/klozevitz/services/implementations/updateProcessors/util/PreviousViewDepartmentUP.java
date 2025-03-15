package org.klozevitz.services.implementations.updateProcessors.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.DepartmentTelegramView;
import org.klozevitz.enitites.appUsers.MessageSent;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.services.interfaces.updateProcessors.BasicUpdateProcessor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.Comparator;

import static org.klozevitz.enitites.appUsers.enums.views.DepartmentView.WELCOME_VIEW;

@Log4j
@RequiredArgsConstructor
public class PreviousViewDepartmentUP extends BasicUpdateProcessor {
    private final String PREVIOUS_VIEW_ERROR_MESSAGE = "<b>Вы совершили действие, которое привело к остановке " +
            "выполнения процесса. Вернемся к предыдущему экрану:</b>";
    private final AppUserRepo appUserRepo;
    private final DepartmentTelegramView telegramView;

    @Override
    public ArrayList<SendMessage> processUpdate(Update update) {
        var tgUserId = telegramUserId(update);
        var currentAppUser = appUserRepo.findByTelegramUserId(tgUserId);
        SendMessage answer;

        if (currentAppUser.isEmpty()) {
            answer = telegramView.notRegisteredDepartmentErrorView(update);

            return answerAsList(answer);
        }

        answer = previousView(update);

        answer = answer.getText().contains(PREVIOUS_VIEW_ERROR_MESSAGE) ?
                answer :
                telegramView.addServiceMessage(answer, PREVIOUS_VIEW_ERROR_MESSAGE);

        return answerAsList(answer);
    }

    private SendMessage previousView(Update update) {
        var telegramUserId = telegramUserId(update);
        var currentAppUser = appUserRepo.findByTelegramUserId(telegramUserId).get();
        var messages = new ArrayList<>(currentAppUser.getMessages());
        var currentView = currentAppUser.getEmployee().getCurrentView();

        if (messages.isEmpty()) {
            currentAppUser.getDepartment().setCurrentView(WELCOME_VIEW);
            appUserRepo.save(currentAppUser);
            return telegramView.previousView(update, currentAppUser);
        }

        messages.sort(Comparator.comparingInt(MessageSent::getMessageId));

        var message = messages.get(messages.size() - 1);
        return message.getAnswer();
    }
}

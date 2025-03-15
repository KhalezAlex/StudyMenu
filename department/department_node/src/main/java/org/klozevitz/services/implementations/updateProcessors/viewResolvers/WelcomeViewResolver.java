package org.klozevitz.services.implementations.updateProcessors.viewResolvers;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.DepartmentTelegramView;
import org.klozevitz.repositories.appUsers.DepartmentRepo;
import org.klozevitz.services.interfaces.updateProcessors.BasicUpdateProcessor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;

import static org.klozevitz.enitites.appUsers.enums.states.DepartmentState.BASIC_STATE;
import static org.klozevitz.enitites.appUsers.enums.views.DepartmentView.WELCOME_VIEW;

@Log4j
@RequiredArgsConstructor
public class WelcomeViewResolver extends BasicUpdateProcessor {
    private final DepartmentRepo departmentRepo;
    private final DepartmentTelegramView telegramView;

    @Override
    public ArrayList<SendMessage> processUpdate(Update update) {
        var telegramUserId = telegramUserId(update);

        departmentRepo.setDepartmentCurrentView(WELCOME_VIEW.name(), telegramUserId);
        departmentRepo.setDepartmentState(BASIC_STATE.name(), telegramUserId);

        var answer = telegramView.welcomeView(update);

        return answerAsList(answer);
    }
}

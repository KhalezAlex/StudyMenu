package org.klozevitz.services.implementations.updateProcessors.viewResolvers;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.EmployeeTelegramView;
import org.klozevitz.repositories.appUsers.EmployeeRepo;
import org.klozevitz.services.interfaces.updateProcessors.UpdateProcessor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;

import static org.klozevitz.enitites.appUsers.enums.states.EmployeeState.BASIC_STATE;
import static org.klozevitz.enitites.appUsers.enums.views.EmployeeView.WELCOME_VIEW;

@Log4j
@RequiredArgsConstructor
public class WelcomeViewResolver implements UpdateProcessor {
    private final EmployeeRepo employeeRepo;
    private final EmployeeTelegramView telegramView;

    @Override
    public ArrayList<SendMessage> processUpdate(Update update) {
        var telegramUserId = telegramUserId(update);
        ArrayList<SendMessage> answer = new ArrayList<>();

        employeeRepo.setEmployeeCurrentView(WELCOME_VIEW.name(), telegramUserId);
        employeeRepo.setEmployeeState(BASIC_STATE.name(), telegramUserId);

        answer.add(
                telegramView.welcomeView(update)
        );

        return answer;
    }
}

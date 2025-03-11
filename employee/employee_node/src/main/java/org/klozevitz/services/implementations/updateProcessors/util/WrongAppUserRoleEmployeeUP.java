package org.klozevitz.services.implementations.updateProcessors.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.EmployeeTelegramView;
import org.klozevitz.services.interfaces.updateProcessors.UpdateProcessor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;

@Log4j
@RequiredArgsConstructor
public class WrongAppUserRoleEmployeeUP implements UpdateProcessor {
    private final EmployeeTelegramView telegramView;

    @Override
    public ArrayList<SendMessage> processUpdate(Update update) {
        ArrayList<SendMessage> answer = new ArrayList<>();

        answer.add(
                telegramView.wrongAppUserRoleErrorView(update)
        );

        return answer;
    }
}

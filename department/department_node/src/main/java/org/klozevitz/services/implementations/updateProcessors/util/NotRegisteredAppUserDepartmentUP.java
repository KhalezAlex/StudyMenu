package org.klozevitz.services.implementations.updateProcessors.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.DepartmentTelegramView;
import org.klozevitz.services.interfaces.updateProcessors.BasicUpdateProcessor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;

@Log4j
@RequiredArgsConstructor
public class NotRegisteredAppUserDepartmentUP extends BasicUpdateProcessor {
    private final DepartmentTelegramView telegramView;

    @Override
    public ArrayList<SendMessage> processUpdate(Update update) {
        var answer = telegramView.notRegisteredDepartmentErrorView(update);

        return answerAsList(answer);
    }
}

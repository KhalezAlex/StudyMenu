package org.klozevitz.services.implementations.updateProcessors.text.byView;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.services.interfaces.updateProcessors.BasicUpdateProcessor;
import org.klozevitz.services.interfaces.updateProcessors.Registrar;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;

@Log4j
@RequiredArgsConstructor
public class EmployeeTgIdRequestViewTUP extends BasicUpdateProcessor {
    private final Registrar employeeRegistrar;
    @Override
    public ArrayList<SendMessage> processUpdate(Update update) {
        var answer = employeeRegistrar.register(update);

        return answerAsList(answer);
    }
}

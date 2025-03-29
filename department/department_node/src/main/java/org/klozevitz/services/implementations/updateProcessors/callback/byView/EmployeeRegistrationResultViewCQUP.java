package org.klozevitz.services.implementations.updateProcessors.callback.byView;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.services.interfaces.updateProcessors.CommandParserUpdateProcessor;
import org.klozevitz.services.interfaces.updateProcessors.UpdateProcessor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;

@Log4j
@RequiredArgsConstructor
public class EmployeeRegistrationResultViewCQUP extends CommandParserUpdateProcessor {
    private final UpdateProcessor previousViewUpdateProcessor;
    private final UpdateProcessor welcomeViewUpdateProcessor;

    @Override
    public ArrayList<SendMessage> processUpdate(Update update) {
        var command = command(update);

        switch (command) {
            case "/start":
                return welcomeViewUpdateProcessor.processUpdate(update);
            default:
                return previousViewUpdateProcessor.processUpdate(update);
        }
    }
}

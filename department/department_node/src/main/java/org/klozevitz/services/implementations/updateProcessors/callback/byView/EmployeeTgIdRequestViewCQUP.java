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
public class EmployeeTgIdRequestViewCQUP extends CommandParserUpdateProcessor {
//    private final AppUserRepo appUserRepo;
    private final UpdateProcessor previousViewUpdateProcessor;
    private final UpdateProcessor welcomeViewResolver;

    @Override
    public ArrayList<SendMessage> processUpdate(Update update) {
//        var telegramUserId = telegramUserId(update);
        var command = command(update);

        switch (command) {
            case "/start":
                return welcomeViewResolver.processUpdate(update);
            default:
                return previousViewUpdateProcessor.processUpdate(update);
        }
    }
}

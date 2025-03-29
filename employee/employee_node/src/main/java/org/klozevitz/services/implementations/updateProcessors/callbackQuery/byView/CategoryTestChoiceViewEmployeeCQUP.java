package org.klozevitz.services.implementations.updateProcessors.callbackQuery.byView;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.services.interfaces.updateProcessors.UpdateProcessor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;

import static org.klozevitz.enitites.appUsers.enums.states.EmployeeState.BASIC_STATE;

@Log4j
@RequiredArgsConstructor
public class CategoryTestChoiceViewEmployeeCQUP implements UpdateProcessor {
    private final AppUserRepo appUserRepo;
    private final UpdateProcessor previousViewUpdateProcessor;
    private final UpdateProcessor welcomeViewResolver;

    @Override
    public ArrayList<SendMessage> processUpdate(Update update) {
        var telegramUserId = telegramUserId(update);
        var optionalCurrentAppUser = appUserRepo.findByTelegramUserId(telegramUserId);

        if (optionalCurrentAppUser.isEmpty()) {
            return previousViewUpdateProcessor.processUpdate(update);
        }

        var state = optionalCurrentAppUser.get().getEmployee().getState();

        if (!state.equals(BASIC_STATE)) {
            return previousViewUpdateProcessor.processUpdate(update);
        }

        var command = command(update);

        if (command.equals("/start")) {
            return welcomeViewResolver.processUpdate(update);
        } else if (command.contains("/category_test_")) {
            // TODO здесь будет начало тестирования
            return previousViewUpdateProcessor.processUpdate(update);
        } else {
            return previousViewUpdateProcessor.processUpdate(update);
        }
    }
}

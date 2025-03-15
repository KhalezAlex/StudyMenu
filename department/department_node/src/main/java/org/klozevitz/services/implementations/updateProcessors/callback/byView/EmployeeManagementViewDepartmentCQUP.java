package org.klozevitz.services.implementations.updateProcessors.callback.byView;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.services.interfaces.updateProcessors.CommandParserUpdateProcessor;
import org.klozevitz.services.interfaces.updateProcessors.UpdateProcessor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;

import static org.klozevitz.enitites.appUsers.enums.states.DepartmentState.BASIC_STATE;

@Log4j
@RequiredArgsConstructor
public class EmployeeManagementViewDepartmentCQUP extends CommandParserUpdateProcessor {
    private final AppUserRepo appUserRepo;
    private final UpdateProcessor previousViewUpdateProcessor;
    private final UpdateProcessor welcomeViewResolver;
    private final UpdateProcessor notRegisteredAppUserUpdateProcessor;
    private final UpdateProcessor employeeTgIdRequestViewResolver;

    @Override
    public ArrayList<SendMessage> processUpdate(Update update) {
        var telegramUserId = telegramUserId(update);
        var optionalCurrentAppUser = appUserRepo.findByTelegramUserId(telegramUserId);

        if (optionalCurrentAppUser.isEmpty()) {
            log.error("Пользователь не зарегистрирован: " + telegramUserId);
            return notRegisteredAppUserUpdateProcessor.processUpdate(update);
        }

        var state = optionalCurrentAppUser.get().getDepartment().getState();

        if (!state.equals(BASIC_STATE)) {
            return previousViewUpdateProcessor.processUpdate(update);
        }

        var command = command(update);

        switch (command) {
            case "/start":
                return welcomeViewResolver.processUpdate(update);
            case "/employee_tg_id_request_view":
                return employeeTgIdRequestViewResolver.processUpdate(update);
            default:
                return previousViewUpdateProcessor.processUpdate(update);
        }
    }
}

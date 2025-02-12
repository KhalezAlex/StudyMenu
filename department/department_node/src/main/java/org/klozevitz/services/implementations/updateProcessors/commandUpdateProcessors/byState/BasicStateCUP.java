package org.klozevitz.services.implementations.updateProcessors.commandUpdateProcessors.byState;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.DepartmentTelegramView;
import org.klozevitz.services.messageProcessors.UpdateProcessor;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.annotation.Resource;
import javax.inject.Inject;

import static org.klozevitz.enitites.appUsers.enums.views.DepartmentView.WELCOME_VIEW;

@Log4j
@RequiredArgsConstructor
public class BasicStateCUP implements UpdateProcessor {
    private final DepartmentTelegramView telegramView;
    @Resource(name = "previousView_UpdateProcessor")
    private UpdateProcessor previousViewUpdateProcessor;
    @Inject
    private AppUserRepo appUserRepo;


    @Override
    public SendMessage processUpdate(Update update, AppUser currentAppUser) {
        var command = update.getMessage().getText();

        switch (command) {
            case "/start":
                return welcomeView(update, currentAppUser);
            default:
                return previousViewUpdateProcessor.processUpdate(update, currentAppUser);
        }
    }

    private SendMessage welcomeView(Update update, AppUser currentAppUser) {
        currentAppUser.getDepartment().setCurrentView(WELCOME_VIEW);
        appUserRepo.save(currentAppUser);

        return telegramView.welcomeView(update);
    }


}

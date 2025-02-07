package org.klozevitz.services.implementations.main;

import lombok.extern.log4j.Log4j;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.enitites.appUsers.Company;
import org.klozevitz.enitites.appUsers.enums.states.CompanyState;
import org.klozevitz.messageProcessors.legacy.CallbackQueryUpdateProcessor;
import org.klozevitz.messageProcessors.legacy.CommandUpdateProcessor;
import org.klozevitz.messageProcessors.legacy.TextUpdateProcessor;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.services.interfaces.main.AnswerProducer;
import org.klozevitz.services.interfaces.main.Main;
import org.klozevitz.messageProcessors.legacy.utils.WrongAppUserRoleUpdateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.HashSet;


@Log4j
@Service
public class MainService implements Main {
    @Autowired
    private AnswerProducer answerProducer;
    @Autowired
    private AppUserRepo appUserRepo;
    @Autowired
    private WrongAppUserRoleUpdateProcessor wrongAppUserRoleUpdateProcessor;
    @Autowired
    private TextUpdateProcessor textUpdateProcessor;
    @Autowired
    @Qualifier("commandUpdateProcessor")
    private CommandUpdateProcessor commandUpdateProcessor;
    @Autowired
    @Qualifier("callbackQueryUpdateProcessor")
    private CallbackQueryUpdateProcessor callbackQueryUpdateProcessor;

    @Override
    public void processTextMessage(Update update) {
        var currentAppUser = findOrSaveAppUser(update);
        var company = currentAppUser.getCompany();

        var answer = company == null ?
                wrongAppUserRoleUpdateProcessor.processUpdate(update) :
                textUpdateProcessor.processTextUpdate(update, currentAppUser);

        sendAnswer(answer);
    }

    @Override
    public void processCommandMessage(Update update) {
        var currentAppUser = findOrSaveAppUser(update);
        var company = currentAppUser.getCompany();

        var answer = company == null ?
                wrongAppUserRoleUpdateProcessor.processUpdate(update) :
                commandUpdateProcessor.processCommandUpdate(update, currentAppUser);

        sendAnswer(answer);
    }

    @Override
    public void processCallbackQueryMessage(Update update) {
        var currentAppUser = findOrSaveAppUser(update);
        var company = currentAppUser.getCompany();

        var answer = company == null ?
                wrongAppUserRoleUpdateProcessor.processUpdate(update) :
                callbackQueryUpdateProcessor.processCallbackQueryUpdate(update, currentAppUser);

        sendAnswer(answer);
    }

    private AppUser findOrSaveAppUser(Update update) {
        var telegramUser = telegramUserFromUpdate(update);
        var persistentApplicationUser = appUserRepo.findByTelegramUserId(telegramUser.getId());

        if (persistentApplicationUser.isEmpty()) {
            return newUnregisteredCompany(telegramUser);
        }

        return persistentApplicationUser.get();
    }

    private User telegramUserFromUpdate(Update update) {
        return update.hasMessage() ?
                update.getMessage().getFrom() :
                update.getCallbackQuery().getFrom();
    }

    private AppUser newUnregisteredCompany(User telegramUser) {
        var transientApplicationUser = AppUser.builder()
                .telegramUserId(telegramUser.getId())
                .username(telegramUser.getUserName())
                .company(
                        Company.builder()
                        .departments(new HashSet<>())
                        .state(CompanyState.UNREGISTERED_STATE)
                        .build()
                )
                .build();

        var persistentAppUser = appUserRepo.save(transientApplicationUser);
        return appUserRepo.save(persistentAppUser);
    }

    private void sendAnswer(SendMessage answer) {
        answerProducer.produceAnswer(answer);
    }
}

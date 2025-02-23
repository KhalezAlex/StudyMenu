package org.klozevitz.services.implementations.main;

import lombok.RequiredArgsConstructor;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.enitites.appUsers.Company;
import org.klozevitz.logger.LoggerInfo;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.services.main.AnswerProducer;
import org.klozevitz.services.interfaces.main.Main;
import org.klozevitz.services.messageProcessors.UpdateProcessor;
import org.klozevitz.services.messageProcessors.WrongAppUserDataUpdateProcessor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.HashSet;

import static org.klozevitz.enitites.appUsers.enums.states.CompanyState.UNREGISTERED_STATE;

@RequiredArgsConstructor
public class MainService implements Main {
    private final AppUserRepo appUserRepo;
    private final AnswerProducer answerProducer;
    private final WrongAppUserDataUpdateProcessor wrongAppUserRoleUpdateProcessor;
    private final UpdateProcessor textUpdateProcessor;
    private final UpdateProcessor commandUpdateProcessor;
    private final UpdateProcessor callbackQueryUpdateProcessor;
    private final LoggerInfo loggerInfo;

    @Override
    public void processTextMessage(Update update) {
        var currentAppUser = findOrSaveAppUser(update);
        var company = currentAppUser.getCompany();

        var answer = company == null ?
                wrongAppUserRoleUpdateProcessor.processUpdate(update) :
                textUpdateProcessor.processUpdate(update, currentAppUser);

        sendAnswer(answer);
        loggerInfo.LoggerInfoUpdate(update);
    }

    @Override
    public void processCommandMessage(Update update) {
        var currentAppUser = findOrSaveAppUser(update);
        var company = currentAppUser.getCompany();

        var answer = company == null ?
                wrongAppUserRoleUpdateProcessor.processUpdate(update) :
                commandUpdateProcessor.processUpdate(update, currentAppUser);

        sendAnswer(answer);
        loggerInfo.LoggerInfoUpdate(update);
    }

    @Override
    public void processCallbackQueryMessage(Update update) {
        var currentAppUser = findOrSaveAppUser(update);
        var company = currentAppUser.getCompany();

        var answer = company == null ?
                wrongAppUserRoleUpdateProcessor.processUpdate(update) :
                callbackQueryUpdateProcessor.processUpdate(update, currentAppUser);

        sendAnswer(answer);
        loggerInfo.LoggerInfoUpdate(update);
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
        var telegramUsername = telegramUser.getUserName();
        var telegramUserId = telegramUser.getId();
        var transientApplicationUser = AppUser.builder()
                .telegramUserId(telegramUserId)
                .username(telegramUsername)
                .build();
        var transientCompany = Company.builder()
                .appUser(transientApplicationUser)
                .state(UNREGISTERED_STATE)
                .departments(new HashSet<>())
                .build();

        transientApplicationUser.setCompany(transientCompany);

        return appUserRepo.save(transientApplicationUser);
    }

    private void sendAnswer(SendMessage answer) {
        answerProducer.produceAnswer(answer);
    }
}

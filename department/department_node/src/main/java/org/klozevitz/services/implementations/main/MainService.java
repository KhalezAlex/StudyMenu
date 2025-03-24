package org.klozevitz.services.implementations.main;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.services.interfaces.main.Main;
import org.klozevitz.services.interfaces.updateProcessors.UpdateProcessor;
import org.klozevitz.services.main.AnswerProducer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.ArrayList;
import java.util.Optional;

@Log4j
@RequiredArgsConstructor
public class MainService implements Main {
    private final AppUserRepo appUserRepo;
    private final AnswerProducer answerProducer;
    private final UpdateProcessor wrongAppUserRoleUpdateProcessor;
    private final UpdateProcessor notRegisteredAppUserUpdateProcessor;
    private final UpdateProcessor commandUpdateProcessor;
    private final UpdateProcessor textUpdateProcessor;
    private final UpdateProcessor callbackQueryUpdateProcessor;
    private final UpdateProcessor documentUpdateProcessor;


    @Override
    public void processTextUpdate(Update update) {
        var optionalCurrentAppUser = findAppUser(update);
        var answer = optionalCurrentAppUser.isEmpty() ?
                notRegisteredAppUserUpdateProcessor.processUpdate(update) :
                registeredUserTextUpdateAnswer(update, optionalCurrentAppUser.get());

        sendAnswer(answer);
    }

    private ArrayList<SendMessage> registeredUserTextUpdateAnswer(Update update, AppUser currentAppUser) {
        var department = currentAppUser.getDepartment();

        return department == null ?
                wrongAppUserRoleUpdateProcessor.processUpdate(update) :
                textUpdateProcessor.processUpdate(update);
    }


    @Override
    public void processCommandUpdate(Update update) {
        var optionalCurrentAppUser = findAppUser(update);
        var answer = optionalCurrentAppUser.isEmpty() ?
                notRegisteredAppUserUpdateProcessor.processUpdate(update) :
                registeredUserCommandUpdateAnswer(update, optionalCurrentAppUser.get());

        sendAnswer(answer);
    }

    private ArrayList<SendMessage> registeredUserCommandUpdateAnswer(Update update, AppUser currentAppUser) {
        var department = currentAppUser.getDepartment();

        return department == null ?
                wrongAppUserRoleUpdateProcessor.processUpdate(update) :
                commandUpdateProcessor.processUpdate(update);
    }

    @Override
    public void processCallbackQueryUpdate(Update update) {
        var optionalCurrentAppUser = findAppUser(update);
        var answer = optionalCurrentAppUser.isEmpty() ?
                notRegisteredAppUserUpdateProcessor.processUpdate(update) :
                registeredUserCallbackQueryUpdateAnswer(update, optionalCurrentAppUser.get());

        sendAnswer(answer);
    }

    private ArrayList<SendMessage> registeredUserCallbackQueryUpdateAnswer(Update update, AppUser currentAppUser) {
        var department = currentAppUser.getDepartment();

        return department == null ?
                wrongAppUserRoleUpdateProcessor.processUpdate(update) :
                callbackQueryUpdateProcessor.processUpdate(update);
    }

    @Override
    public void processDocUpdate(Update update) {
        var optionalCurrentAppUser = findAppUser(update);
        var answer = optionalCurrentAppUser.isEmpty() ?
                notRegisteredAppUserUpdateProcessor.processUpdate(update) :
                registeredUserDocUpdateAnswer(update, optionalCurrentAppUser.get());

        sendAnswer(answer);
    }

    private ArrayList<SendMessage> registeredUserDocUpdateAnswer(Update update, AppUser currentAppUser) {
        var department = currentAppUser.getDepartment();

        return department == null ?
                wrongAppUserRoleUpdateProcessor.processUpdate(update) :
                documentUpdateProcessor.processUpdate(update);
    }

    private Optional<AppUser> findAppUser(Update update) {
        var telegramUser = telegramUserFromUpdate(update);
        var persistentApplicationUser = appUserRepo.findByTelegramUserId(telegramUser.getId());

        if (persistentApplicationUser.isEmpty()) {
            return Optional.empty();
        }

        if (persistentApplicationUser.get().getDepartment() == null) {
            return Optional.empty();
        }

        return persistentApplicationUser;
    }

    private User telegramUserFromUpdate(Update update) {
        return update.hasMessage() ?
                update.getMessage().getFrom() :
                update.getCallbackQuery().getFrom();
    }

    private void sendAnswer(ArrayList<SendMessage> answer) {
        answer.forEach(answerProducer::produceAnswer);
    }
}

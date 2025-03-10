package org.klozevitz.services.implementations.main;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.services.interfaces.main.AnswerProducer;
import org.klozevitz.services.interfaces.main.Main;
import org.klozevitz.services.interfaces.updateProcessors.UpdateProcessor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Optional;

@Log4j
@RequiredArgsConstructor
public class MainService implements Main {
    private final AppUserRepo appUserRepo;
    private final AnswerProducer answerProducer;
    private final UpdateProcessor wrongAppUserRoleUpdateProcessor;
    private final UpdateProcessor notRegisteredAppUserUpdateProcessor;

    private final UpdateProcessor commandUpdateProcessor;
    private final UpdateProcessor callbackQueryUpdateProcessor;
//    private final UpdateProcessor<Update, AppUser> textUpdateProcessor;



    @Override
    public void processTextUpdate(Update update) {
        var optionalCurrentAppUser = findAppUser(update);
        var answer = optionalCurrentAppUser.isEmpty() ?
                notRegisteredAppUserUpdateProcessor.processUpdate(update) :
                registeredTextUpdateAnswer(update, optionalCurrentAppUser.get());

        sendAnswer(answer);
    }

    private SendMessage registeredTextUpdateAnswer(Update update, AppUser currentAppUser) {
//        var employee = currentAppUser.getEmployee();
//
//        return employee == null ?
//                wrongAppUserRoleUpdateProcessor.processUpdate(update, null) :
//                textUpdateProcessor.processUpdate(update, currentAppUser);
        return null;
    }

    @Override
    public void processCommandUpdate(Update update) {
        var optionalCurrentAppUser = findAppUser(update);
        var answer = optionalCurrentAppUser.isEmpty() ?
                notRegisteredAppUserUpdateProcessor.processUpdate(update) :
                registeredCommandUpdateAnswer(update, optionalCurrentAppUser.get());

        sendAnswer(answer);
    }

    private SendMessage registeredCommandUpdateAnswer(Update update, AppUser currentAppUser) {
        var employee = currentAppUser.getEmployee();

        return employee == null ?
                wrongAppUserRoleUpdateProcessor.processUpdate(update) :
                commandUpdateProcessor.processUpdate(update);
    }

    @Override
    public void processCallbackQueryUpdate(Update update) {
        var optionalCurrentAppUser = findAppUser(update);
        var answer = optionalCurrentAppUser.isEmpty() ?
                notRegisteredAppUserUpdateProcessor.processUpdate(update) :
                registeredCallbackQueryUpdateAnswer(update, optionalCurrentAppUser.get());

        sendAnswer(answer);
    }

    private SendMessage registeredCallbackQueryUpdateAnswer(Update update, AppUser currentAppUser) {
        var employee = currentAppUser.getEmployee();

        return employee == null ?
                wrongAppUserRoleUpdateProcessor.processUpdate(update) :
                callbackQueryUpdateProcessor.processUpdate(update);
    }

    private Optional<AppUser> findAppUser(Update update) {
        var telegramUser = telegramUserFromUpdate(update);
        var persistentApplicationUser = appUserRepo.findByTelegramUserId(telegramUser.getId());

        if (persistentApplicationUser.isEmpty()) {
            return Optional.empty();
        }

        if (persistentApplicationUser.get().getEmployee() == null) {
            return Optional.empty();
        }

        return persistentApplicationUser;
    }

    private User telegramUserFromUpdate(Update update) {
        return update.hasMessage() ?
                update.getMessage().getFrom() :
                update.getCallbackQuery().getFrom();
    }

    private void sendAnswer(SendMessage answer) {
        answerProducer.produceAnswer(answer);
    }
}

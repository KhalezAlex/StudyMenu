package org.klozevitz.services.implementations.main;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.services.messageProcessors.UpdateProcessor;
import org.klozevitz.services.messageProcessors.WrongAppUserDataUpdateProcessor;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.services.interfaces.main.AnswerProducer;
import org.klozevitz.services.interfaces.main.Main;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import javax.annotation.Resource;
import java.util.Optional;

@Log4j
@Service
@RequiredArgsConstructor
public class MainService implements Main {
    private final AppUserRepo appUserRepo;
    private final AnswerProducer answerProducer;
    @Resource(name = "wrongAppUserRoleUpdateProcessor")
    private WrongAppUserDataUpdateProcessor wrongAppUserRoleUpdateProcessor;
    @Resource(name = "notRegisteredAppUserUpdateProcessor")
    private WrongAppUserDataUpdateProcessor notRegisteredAppUserUpdateProcessor;
    @Resource(name = "commandUpdateProcessor")
    private UpdateProcessor commandUpdateProcessor;
    @Resource(name = "textUpdateProcessor")
    private UpdateProcessor textUpdateProcessor;
    @Resource(name = "callbackQueryUpdateProcessor")
    private UpdateProcessor callbackQueryUpdateProcessor;


    @Override
    public void processTextUpdate(Update update) {
        var optionalCurrentAppUser = findAppUser(update);
        var answer = optionalCurrentAppUser.isEmpty() ?
                notRegisteredAppUserUpdateProcessor.processUpdate(update) :
                registeredUserTextUpdateAnswer(update, optionalCurrentAppUser.get());

        sendAnswer(answer);
    }

    private SendMessage registeredUserTextUpdateAnswer(Update update, AppUser currentAppUser) {
        var department = currentAppUser.getDepartment();

        return department == null ?
                wrongAppUserRoleUpdateProcessor.processUpdate(update) :
                textUpdateProcessor.processUpdate(update, currentAppUser);
    }


    @Override
    public void processCommandUpdate(Update update) {
        var optionalCurrentAppUser = findAppUser(update);
        var answer = optionalCurrentAppUser.isEmpty() ?
                notRegisteredAppUserUpdateProcessor.processUpdate(update) :
                registeredUserCommandUpdateAnswer(update, optionalCurrentAppUser.get());

        sendAnswer(answer);
    }

    private SendMessage registeredUserCommandUpdateAnswer(Update update, AppUser currentAppUser) {
        var department = currentAppUser.getDepartment();

        return department == null ?
                wrongAppUserRoleUpdateProcessor.processUpdate(update) :
                commandUpdateProcessor.processUpdate(update, currentAppUser);

    }

    @Override
    public void processCallbackQueryUpdate(Update update) {
        var optionalCurrentUser = findAppUser(update);
        var answer = optionalCurrentUser.isEmpty() ?
                notRegisteredAppUserUpdateProcessor.processUpdate(update) :
                registeredUserCallbackQueryUpdateAnswer(update, optionalCurrentUser.get());

        sendAnswer(answer);
    }

    private SendMessage registeredUserCallbackQueryUpdateAnswer(Update update, AppUser currentAppUser) {
        var department = currentAppUser.getDepartment();

        return department == null ?
                wrongAppUserRoleUpdateProcessor.processUpdate(update) :
                callbackQueryUpdateProcessor.processUpdate(update, currentAppUser);
    }

    @Override
    public void processDocUpdate(Update update) {

    }

    private long chatId(Update update) {
        return update.hasMessage() ?
                update.getMessage().getChatId() :
                update.getCallbackQuery().getMessage().getChatId();
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

    private void sendAnswer(SendMessage answer) {
        answerProducer.produceAnswer(answer);
    }
}

package org.klozevitz.services.implementations.main;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.services.interfaces.main.Main;
import org.klozevitz.services.main.AnswerProducer;
import org.klozevitz.services.messageProcessors.UpdateProcessor;
import org.klozevitz.services.messageProcessors.WrongAppUserDataUpdateProcessor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Optional;

@Log4j
@RequiredArgsConstructor
public class MainService implements Main {
    private final AppUserRepo appUserRepo;
    private final AnswerProducer answerProducer;
    private final WrongAppUserDataUpdateProcessor wrongAppUserRoleUpdateProcessor;
    private final WrongAppUserDataUpdateProcessor notRegisteredAppUserUpdateProcessor;

    private final UpdateProcessor commandUpdateProcessor;
    private final UpdateProcessor callbackQueryUpdateProcessor;
    private final UpdateProcessor textUpdateProcessor;



    @Override
    public void processTextUpdate(Update update) {
        log.debug("text");
        var optionalCurrentAppUser = findAppUser(update);
        var answer = optionalCurrentAppUser.isEmpty() ?
                notRegisteredAppUserUpdateProcessor.processUpdate(update) :
                registeredTextUpdateAnswer(update, optionalCurrentAppUser.get());

        sendAnswer(answer);
    }

    private SendMessage registeredTextUpdateAnswer(Update update, AppUser currentAppUser) {
        var employee = currentAppUser.getEmployee();

        return employee == null ?
                wrongAppUserRoleUpdateProcessor.processUpdate(update) :
                textUpdateProcessor.processUpdate(update, currentAppUser);
    }

    @Override
    public void processCommandUpdate(Update update) {
        log.debug("command");
    }

    @Override
    public void processCallbackQueryUpdate(Update update) {
        log.debug("callback query");
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

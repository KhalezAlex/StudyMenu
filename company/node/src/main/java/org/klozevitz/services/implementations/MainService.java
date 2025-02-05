package org.klozevitz.services.implementations;

import lombok.extern.log4j.Log4j;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.enitites.appUsers.Company;
import org.klozevitz.enitites.appUsers.enums.states.CompanyState;
import org.klozevitz.messageProcessors.CallbackQueryUpdateProcessor;
import org.klozevitz.messageProcessors.CommandUpdateProcessor;
import org.klozevitz.messageProcessors.TextUpdateProcessor;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.services.interfaces.AnswerProducer;
import org.klozevitz.services.interfaces.Main;
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
    private TextUpdateProcessor textUpdateProcessor;
    @Autowired
    @Qualifier("commandUpdateProcessor")
    private CommandUpdateProcessor commandUpdateProcessor;
    @Autowired
    @Qualifier("callbackQueryUpdateProcessor")
    private CallbackQueryUpdateProcessor callbackQueryUpdateProcessor;

    @Override
    public void processTextMessage(Update update) {
        var chatId = chatId(update);
        var appUser = findOrSaveAppUser(update);
        var answer = textUpdateProcessor.processTextMessage(update, appUser);
        sendAnswer(answer, chatId);
    }

    @Override
    public void processCommandMessage(Update update) {
        var chatId = chatId(update);
        var appUser = findOrSaveAppUser(update);
        var answer = commandUpdateProcessor.processCommandMessage(update, appUser);
        sendAnswer(answer, chatId);
    }

    @Override
    public void processCallbackQueryMessage(Update update) {
        var chatId = chatId(update);
        var appUser = findOrSaveAppUser(update);
        var answer = callbackQueryUpdateProcessor.processCallbackQueryMessage(update, appUser);
        sendAnswer(answer, chatId);
    }

    private long chatId(Update update) {
        return update.hasMessage() ?
                update.getMessage().getChatId() :
                update.getCallbackQuery().getMessage().getChatId();
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

    private void sendAnswer(SendMessage output, Long chatId) {
        answerProducer.produceAnswer(output);
    }
}

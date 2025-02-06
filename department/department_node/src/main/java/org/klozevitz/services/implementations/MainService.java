package org.klozevitz.services.implementations;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.messageProcessors.TextUpdateProcessor;
import org.klozevitz.messageProcessors.WrongAppUserRoleUpdateProcessor;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.services.interfaces.AnswerProducer;
import org.klozevitz.services.interfaces.Main;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Optional;

@Log4j
@Service
@RequiredArgsConstructor
public class MainService  implements Main {
    private final AppUserRepo appUserRepo;
    private final AnswerProducer answerProducer;
    private final WrongAppUserRoleUpdateProcessor wrongAppUserRoleUpdateProcessor;
    private final TextUpdateProcessor textUpdateProcessor;


    @Override
    public void processTextMessage(Update update) {
        var currentAppUser = findAppUser(update).get();
        var department = currentAppUser.getDepartment();

        var answer = department == null ?
                wrongAppUserRoleUpdateProcessor.processUpdate(update) :
                textUpdateProcessor.processTextUpdate(update, currentAppUser);

        sendAnswer(answer);
    }

    @Override
    public void processCommandMessage(Update update) {

    }

    @Override
    public void processCallbackQueryMessage(Update update) {

    }

    @Override
    public void processDocMessage(Update update) {

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

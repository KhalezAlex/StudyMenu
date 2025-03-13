package org.klozevitz.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.enitites.appUsers.MessageSent;
import org.klozevitz.interfaces.ViewManager;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.repositories.appUsers.MessageSentRepo;
import org.klozevitz.telegram_component.DepartmentTelegramBot;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Log4j
@Component
@RequiredArgsConstructor
public class DepartmentViewManager implements ViewManager {
    private final MessageSentRepo messageSentRepo;
    private final AppUserRepo appUserRepo;
    private DepartmentTelegramBot bot;

    public void registerBot(DepartmentTelegramBot bot) {
        this.bot = bot;
    }

    @Override
    public void saveMessageId(SendMessage answer, int sentMessageId) {
        var telegramUserId = telegramUserId(answer);
        var optionalCurrentAppUser = appUserRepo.findByTelegramUserId(telegramUserId);
        var currentAppUser = optionalCurrentAppUser.get();

        currentAppUser.getMessages().add(
                MessageSent.builder()
                        .answer(answer)
                        .messageId(sentMessageId)
                        .appUser(currentAppUser)
                        .build()
        );

        appUserRepo.save(currentAppUser);
    }

    @Override
    public void flushHistory(long telegramUserId) {

    }

    private long telegramUserId(SendMessage sendMessage) {
        return Long.parseLong(sendMessage.getChatId());
    }
}

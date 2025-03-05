package org.klozevitz.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.enitites.appUsers.MessageSent;
import org.klozevitz.interfaces.ViewManager;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.repositories.appUsers.MessageSentRepo;
import org.klozevitz.telegram_component.EmployeeTelegramBot;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Comparator;

@Log4j
@Component
@RequiredArgsConstructor
public class EmployeeViewManager implements ViewManager {
    private final MessageSentRepo messageSentRepo;
    private final AppUserRepo appUserRepo;
    private EmployeeTelegramBot bot;

    public void registerBot(EmployeeTelegramBot bot) {
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
        appUserRepo.save(optionalCurrentAppUser.get());
    }

    @Override
    public void flushHistory(long telegramUserId) {
        var persistentMessages = appUserRepo
                .findByTelegramUserId(telegramUserId)
                .get()
                .getMessages();
        ArrayList<MessageSent> messages = new ArrayList<>(persistentMessages);

        messages.sort(Comparator.comparingInt(MessageSent::getMessageId));

        int persistentMessageTgMessageId = -1;
        while (!(messages.size() == 1)) {
            var persistentMessage = messages.get(0);
            persistentMessageTgMessageId = persistentMessage.getMessageId();
            delete(telegramUserId, persistentMessageTgMessageId);
            messageSentRepo.deleteMessageById(persistentMessage.getId());
            messages.remove(0);
        }
        delete(telegramUserId, persistentMessageTgMessageId - 1);
    }

    private void delete(long telegramUserId, int messageId) {
        var deleteMessage = deleteMessage(telegramUserId, messageId);
        try {
            bot.execute(deleteMessage);
        } catch (TelegramApiException ignored) {
            log.error("Уже удалено!!!");
        }
    }

    private DeleteMessage deleteMessage(long telegramUserId, int messageId) {
        return DeleteMessage.builder()
                .chatId(telegramUserId)
                .messageId(messageId)
                .build();
    }

    private long telegramUserId(SendMessage sendMessage) {
        return Long.parseLong(sendMessage.getChatId());
    }
}

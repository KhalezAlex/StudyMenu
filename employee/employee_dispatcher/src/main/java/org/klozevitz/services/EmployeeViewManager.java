package org.klozevitz.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.enitites.appUsers.MessageId;
import org.klozevitz.interfaces.ViewManager;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.repositories.appUsers.MessageIdRepo;
import org.klozevitz.telegram_component.EmployeeTelegramBot;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.stream.Collectors;

@Log4j
@Component
@RequiredArgsConstructor
public class EmployeeViewManager implements ViewManager {
    private final MessageIdRepo messageIdRepo;
    private final AppUserRepo appUserRepo;
    private EmployeeTelegramBot bot;

    public void registerBot(EmployeeTelegramBot bot) {
        this.bot = bot;
    }

    @Override
    public void saveMessageId(Message messageSent) {
        var telegramUserId = messageSent.getChatId();
        var currentAppUser = appUserRepo.findByTelegramUserId(telegramUserId);
        messageIdRepo.save(currentAppUser.get().getId(), messageSent.getMessageId());
    }

    @Override
    public void flushHistory(long telegramUserId) {
        var messageIds = appUserRepo
                .findByTelegramUserId(telegramUserId)
                .get()
                .getMessages()
                .stream()
                .map(MessageId::getMessageId)
                .collect(Collectors.toList());
        if (messageIds.isEmpty()) {
            return;
        }

        messageIds.sort(Integer::compare);

        int messageId = -1;
        while (!(messageIds.size() == 1)) {
            messageId = messageIds.get(0);
            delete(telegramUserId, messageId);
            messageIdRepo.deleteMessageIdByMessageId(messageId);
            messageIds.remove(0);
        }
        delete(telegramUserId, messageId - 1);
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
}

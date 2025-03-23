package org.klozevitz.telegram_bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.PreCheckoutQuery;
import com.pengrad.telegrambot.model.SuccessfulPayment;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.LabeledPrice;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.*;
import lombok.Builder;
import lombok.extern.log4j.Log4j;
import org.klozevitz.service.OrderService;
import org.klozevitz.service.SubscriptionService;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Log4j
public class TelegramBotApplication extends TelegramBot {
    private final String providerToken;
    private final ExecutorService executorService;
    private final SubscriptionService subscriptionService;
    private final OrderService orderService;

    @lombok.Builder
    public TelegramBotApplication(String botToken, String providerToken) {
        super(botToken);
        this.providerToken = Optional.ofNullable(providerToken).orElse("");
        this.executorService = Executors.newFixedThreadPool(8);
        this.subscriptionService = SubscriptionService.getInstance();
        this.orderService = OrderService.getInstance();
    }

    public void run() {
        this.setUpdatesListener(updates -> {
            updates.stream()
                    .<Runnable> map(update -> () -> process(update))
                    .forEach(executorService::submit);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        }, exception -> System.out.println(exception.response().description()));
    }

    private void process(Update update) {
        Message message = update.message();
        if (message != null) {
            Optional.ofNullable(message.text())
                    .ifPresent(commandName -> this.serveCommand(commandName, message.chat().id()));
            if (message.successfulPayment() != null) {
                Optional.ofNullable(message.successfulPayment())
                        .ifPresent(payment -> servePayment(payment, message.chat().id()));
                log.info("SuccessfulPayment: " + update.message().successfulPayment() + " | id: " + update.message().from().id());
            }
        }
        else if (update.preCheckoutQuery() != null) {
            PreCheckoutQuery preCheckoutQuery = update.preCheckoutQuery();
            execute(new AnswerPreCheckoutQuery(preCheckoutQuery.id()));
        }
    }

    private void servePayment(SuccessfulPayment payment, Long id) {
        orderService.createPurchase(payment, id);
    }
    //TODO подумать как показывать сперва только базовую подписку,
    // а когда она будет подключена показывать дополнительные иначе можем ловить баги когда дополнительные подписки оплачивают без основной
    private void serveCommand(String commandName, Long chatId) {
        switch (commandName) {
            case "/payment": {
                SendMessage response = new SendMessage(chatId, "Чтобы начать пользоваться нашим сервисом, " +
                        "необходимо оплатить \"Базовую подписку\", для увеличения численности сотрудников " +
                        "необходимо дополнительно приобрести соответствующую подписку в \"Дополнительные подписки\"")
                        .replyMarkup(new ReplyKeyboardMarkup(new String[][] {
                                {"Базовая подписка", "Дополнительные подписки"},
                                {"Помощь"}
                        }).resizeKeyboard(true));
                this.execute(response);
                break;
            }
            case "Базовая подписка": {
                subscriptionService.baseSubscription()
                        .forEach(subscription -> {
                            SendInvoice sendInvoice = new SendInvoice(chatId, subscription.getName(), subscription.getDescription(),
                                    subscription.getId(), providerToken, "my_start_param", "RUB",
                                    new LabeledPrice("Price", subscription.getPrice().multiply(BigDecimal.valueOf(100L)).intValue()))
                                    .replyMarkup(new InlineKeyboardMarkup(new InlineKeyboardButton("Оплатить").pay()));
                            execute(sendInvoice);
                        });
                break;
            }
            case "Дополнительные подписки": {
                subscriptionService.additionalSubscription()
                        .forEach(subscription -> {
                            SendInvoice sendInvoice = new SendInvoice(chatId, subscription.getName(), subscription.getDescription(),
                                    subscription.getId(), providerToken, "my_start_param", "RUB",
                                    new LabeledPrice("Price", subscription.getPrice().multiply(BigDecimal.valueOf(100L)).intValue()))
                                    .replyMarkup(new InlineKeyboardMarkup(new InlineKeyboardButton("Оплатить").pay()));
                            execute(sendInvoice);
                        });
                break;
            }
//            case "Помощь": {
//
//            }
            default: {
                SendMessage response = new SendMessage(chatId, "Команда не распознана, " +
                        "для оплаты введите команду \n/payment, " +
                        "если вам нужна помощь введите \n/help ");
                this.execute(response);
                break;
            }
        }
    }
}

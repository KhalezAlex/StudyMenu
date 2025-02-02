package org.klozevitz;

import lombok.AllArgsConstructor;
import org.klozevitz.enitites.appUsers.AppUser;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

@AllArgsConstructor
public class TelegramView {
    private final String UNREGISTERED_WELCOME_MESSAGE = "Вы находитесь на главной странице чат-бота.\n" +
            "Ваша компания еще не зарегистрирована.\n" +
            "Нажмите кнопку для продолжения регистрации.";
    private final String EMAIL_REQUEST_MESSAGE = "Введите адрес электронной почты для завершения регистрации";
    private final String EMAIL_CONFIRMATION_REQUEST_MESSAGE = "Ссылка на подтверждение регистрации выслана " +
            "на указанный Вами ящик электронной почты.\n" +
            "Для подтверждения регистрации пройдите по ссылке в письме.\n\n" +
            "Если требуется изменить адрес, нажмите кнопку внизу:";
    private final String EMAIL_CONFIRMATION_NOTIFICATION_MESSAGE = "Регистрация завершена!\n" +
            "Вам доступно полноценное пользование ботом.\n" +
            "Для того, чтобы попасть на главный экран, нажмите на кнопку внизу.";
    private final String EMAIL_CANNOT_BE_SENT_MESSAGE = "Письмо с инструкциями по валидации " +
            "не может быть направлено на данную электронную почту: %s\n" +
            "Повторите процедуру регистрации.\n";
    private final String WRONG_EMAIL_NOTIFICATION_MESSAGE = "Адрес, который Вы ввели, не является " +
            "валидным адресом электронной почты!";
    private final String ALREADY_REGISTERED_EMAIL_NOTIFICATION_MESSAGE = "Адрес, который Вы ввели, " +
            "уже зарегистрирован в системе";
    private final String PREVIOUS_VIEW_ERROR_MESSAGE = "Вы совершили действие, которое привело к остановке" +
            "выполнения процесса. Вернемся к предыдущему экрану:";
    private final String NULL_COMPANY_STATE_ERROR_NOTIFICATION_MESSAGE = "Произошла непредвиденная ошибка. " +
            "Свяжитесь со специалистом технической поддержки для устранения ошибки.";
    private MessageUtil messageUtil;

    /**
     * Метод получает пользователя и объект update и выдает сообщение с вьюхой, с которой пользователь уходит,
     * чтобы вернуться назад
     * */

    public SendMessage previousView(Update update, AppUser appUser) {
        var answer = previousViewStrategy(update, appUser);

        return messageUtil.addErrorMessage(answer, PREVIOUS_VIEW_ERROR_MESSAGE);
    }

    private SendMessage previousViewStrategy(Update update, AppUser appUser) {
        var view = appUser.getCompany().getCurrentView();

        switch (view) {
            case UNREGISTERED_WELCOME_VIEW:
                return unregisteredWelcomeView(update);
            case EMAIL_REQUEST_VIEW:
                return emailRequestView(update);
            case EMAIL_CONFIRMATION_REQUEST_VIEW:
                return emailConfirmationRequestView(update);
            default:
                return null;
        }
    }

    /**
     * Первое приветственное сообщение
     * */
    public SendMessage unregisteredWelcomeView(Update update) {
        var chatId = chatId(update);
        var unregisteredWelcomeView = new SendMessage();
        var unregisteredInlineKeyboardMarkup = unregisteredInlineKeyboardMarkup();

        unregisteredWelcomeView.setText(UNREGISTERED_WELCOME_MESSAGE);
        unregisteredWelcomeView.setReplyMarkup(unregisteredInlineKeyboardMarkup);
        unregisteredWelcomeView.setChatId(chatId);

        return unregisteredWelcomeView;
    }

    private InlineKeyboardMarkup unregisteredInlineKeyboardMarkup() {
        var keyboard = new InlineKeyboardMarkup();
        var row = List.of(
                button("РЕГИСТРАЦИЯ", "/register")
        );
        var keyboardRows = List.of(row);
        keyboard.setKeyboard(keyboardRows);
        return keyboard;
    }

    private InlineKeyboardButton button(String text, String callbackData) {
        var button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callbackData);
        return button;
    }


    /**
     * Вьюхи, связанные с регистрацией
     * */
    public SendMessage emailRequestView(Update update) {
        var chatId = chatId(update);

        return textMessage(EMAIL_REQUEST_MESSAGE, chatId);
    }

    public SendMessage emailConfirmationRequestView(Update update) {
        var answer = messageUtil.blankAnswer(update);
        var successNotificationViewAbortRegistrationKeyboardMarkup =
                successNotificationViewAbortRegistrationKeyboardMarkup();

        answer.setText(EMAIL_CONFIRMATION_REQUEST_MESSAGE);
        answer.setReplyMarkup(successNotificationViewAbortRegistrationKeyboardMarkup);

        return answer;
    }

    private InlineKeyboardMarkup successNotificationViewAbortRegistrationKeyboardMarkup() {
        var keyboard = new InlineKeyboardMarkup();
        var row = List.of(
                button("ЗАРЕГИСТРИРОВАТЬСЯ ЗАНОВО", "abort_registration")
        );
        var keyboardRows = List.of(row);
        keyboard.setKeyboard(keyboardRows);
        return keyboard;
    }

    public SendMessage emailCanNotBeSentNotificationView(Update update) {
        var email = update.getMessage().getText();
        var errorMessage = String.format(EMAIL_CANNOT_BE_SENT_MESSAGE, email);

        var answer = unregisteredWelcomeView(update);
        return messageUtil.addErrorMessage(answer, errorMessage);
    }

    public SendMessage wrongEmailNotificationView(Update update) {
        var answer = emailRequestView(update);

        return messageUtil.addErrorMessage(answer, WRONG_EMAIL_NOTIFICATION_MESSAGE);
    }

    public SendMessage alreadyRegisteredEmailNotificationView(Update update) {
        var answer = emailRequestView(update);

        return messageUtil.addErrorMessage(answer, ALREADY_REGISTERED_EMAIL_NOTIFICATION_MESSAGE);
    }

    public SendMessage emailConfirmationNotificationView(long chatId) {
        var answer = messageUtil.blankAnswer(chatId);
        var emailConfirmationNotificationViewWelcomeViewKeyboardMarkup =
                emailConfirmationNotificationViewWelcomeViewKeyboardMarkup();

        answer.setText(EMAIL_CONFIRMATION_NOTIFICATION_MESSAGE);
        answer.setReplyMarkup(emailConfirmationNotificationViewWelcomeViewKeyboardMarkup);

        return answer;
    }

    public InlineKeyboardMarkup emailConfirmationNotificationViewWelcomeViewKeyboardMarkup() {
        var keyboard = new InlineKeyboardMarkup();
        var row = List.of(
                button("ОСНОВНОЙ ЭКРАН", "start")
        );
        var keyboardRows = List.of(row);
        keyboard.setKeyboard(keyboardRows);
        return keyboard;
    }


    /**
     * Вьюхи для зарегистрированных пользователей
     * */

    public SendMessage registeredWelcomeView(Update update) {
        var message = "Ты зарегистрирован! следующим этапом пишем эту страницу!!!";
        var chatId = chatId(update);
        var answer = new SendMessage();

        answer.setText(message);
        answer.setChatId(chatId);

        return answer;
    }

    /**
     * Базовое текстовое сообщение
     * */
    public SendMessage textMessage(String message, Long chatId) {
        SendMessage answer = new SendMessage();
        answer.setChatId(chatId);
        answer.setText(message);
        return answer;
    }

    /**
     * Сообщение об ошибке в результате потери статуса
     * */

    public SendMessage nullCompanyStateNotificationMessage(Update update) {
        var chatId = chatId(update);

        return textMessage(NULL_COMPANY_STATE_ERROR_NOTIFICATION_MESSAGE, chatId);
    }

    /**
     * UTILS
     * */

    private long chatId(Update update) {
        return update.hasMessage() ?
                update.getMessage().getChatId() :
                update.getCallbackQuery().getMessage().getChatId();
    }
}

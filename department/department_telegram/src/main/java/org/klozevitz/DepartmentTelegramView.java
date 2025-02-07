package org.klozevitz;

import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

@RequiredArgsConstructor
public class DepartmentTelegramView {
    private final String WRONG_APP_USER_ROLE_ERROR_MESSAGE = "Вы зарегистрированы, как персонал или как человек, " +
            "отвечающий за компанию в целом- функционал этого чата для Вас не доступен.\n" +
            "Если Вам необходимо зарегистрироваться, как руководитель отделения, попросите организацию, " +
            "зарегистрировавшую Вас, удалить Вас или переназначить руководство на другой аккаунт телеграм.";
    private final String NULL_DEPARTMENT_STATE_ERROR_MESSAGE = "<b>Произошла непредвиденная ошибка. " +
            "Свяжитесь со специалистом технической поддержки для устранения ошибки.</b>";
    private final String NOT_REGISTERED_DEPARTMENT_ERROR_MESSAGE = "Вы не зарегистрированы, как человек, отвечающий " +
            "за отделение компании. Обратитесь к Вашему руководителю и предоставьте ему свой телеграмм-id " +
            "для регистрации";
    private final String WELCOME_MESSAGE = "Вы находитесь на главной странице чат-бота.\n" +
            "Вам доступны следующие действия:";
    private final MessageUtil messageUtil;



    /**
     * Сообщение об ошибке статуса пользователя в приложении- если он зарегистрирован в другом чате
     * и попробовал воспользоваться функционалом этого
     * DepartmentView.WRONG_APP_USER_ROLE_ERROR_VIEW
     * */
    public SendMessage wrongAppUserRoleErrorView(Update update) {
        var answer = messageUtil.blankAnswer(update);

        answer.setText(WRONG_APP_USER_ROLE_ERROR_MESSAGE);

        return answer;
    }

    /**
     * Сообщение об ошибке регистрации- выскакивает, если пользователя вообще нет в базе данных
     * DepartmentView.NOT_REGISTERED_DEPARTMENT_ERROR_VIEW
     * */
    public SendMessage notRegisteredDepartmentErrorView(Update update) {
        var answer = messageUtil.blankAnswer(update);

        answer.setText(NOT_REGISTERED_DEPARTMENT_ERROR_MESSAGE);

        return answer;
    }

    /**
     * Первое приветственное сообщение
     * DepartmentView.WELCOME_VIEW
     * */
    public SendMessage welcomeView(Update update) {
        var answer = messageUtil.blankAnswer(update);
        var welcomeViewKeyboardMarkup = welcomeViewKeyboardMarkup();

        answer.setText(WELCOME_MESSAGE);
        answer.setReplyMarkup(welcomeViewKeyboardMarkup);

        return answer;
    }

    private InlineKeyboardMarkup welcomeViewKeyboardMarkup() {
        var keyboard = new InlineKeyboardMarkup();
        var keyboardRows = List.of(
                List.of(
                    button("УПРАВЛЕНИЕ ПЕРСОНАЛОМ", "/employee_management")
                ),
                List.of(
                    button("УПРАВЛЕНИЕ МАТЕРИАЛАМИ", "/material_management")
                ),
                List.of(
                    button("УПРАВЛЕНИЕ ПРОФИЛЕМ", "/profile_management")
                )
        );

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
     * ПО ИДЕЕ, ВООБЩЕ НИКОГДА НЕ ДОЛЖНО ВЫСКАКИВАТЬ- ЭТО ПРОВЕРКА СЛУЧАЙНЫХ СИТУАЦИЙ
     * CompanyView.NULL_DEPARTMENT_STATE_ERROR_VIEW
     * */

    public SendMessage nullDepartmentStateErrorView(Update update) {
        var chatId = chatId(update);

        return textMessage(NULL_DEPARTMENT_STATE_ERROR_MESSAGE, chatId);
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

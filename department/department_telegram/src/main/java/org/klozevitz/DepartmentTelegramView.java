package org.klozevitz;

import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

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

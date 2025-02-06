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
    private final String NULL_COMPANY_STATE_ERROR_MESSAGE = "<b>Произошла непредвиденная ошибка. " +
            "Свяжитесь со специалистом технической поддержки для устранения ошибки.</b>";

    private final MessageUtil messageUtil;



    /**
     * Вью предназначен для тех, кто имеет аккаунт в другом чате, и попробовал воспользоваться функционалом текущего.
     * СВОЕГО ВЬЮ в CompanyView нет
     * */
    public SendMessage wrongAppUserRoleErrorView(Update update) {
        var answer = messageUtil.blankAnswer(update);

        answer.setText(WRONG_APP_USER_ROLE_ERROR_MESSAGE);

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
     * CompanyView.NULL_COMPANY_STATE_NOTIFICATION_VIEW
     * */

    public SendMessage nullCompanyStateNotificationView(Update update) {
        var chatId = chatId(update);

        return textMessage(NULL_COMPANY_STATE_ERROR_MESSAGE, chatId);
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

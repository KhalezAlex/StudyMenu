package org.klozevitz;

import lombok.RequiredArgsConstructor;
import org.klozevitz.enitites.appUsers.enums.views.EmployeeView;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;

@RequiredArgsConstructor
public class EmployeeTelegramView {
    private final String PREVIOUS_VIEW_ERROR_MESSAGE = "<b>Вы совершили действие, которое привело к остановке " +
            "выполнения процесса. Вернемся к предыдущему экрану:</b>";
    private final String NULL_CURRENT_VIEW_ERROR_MESSAGE = "<b>ТАКОГО НЕ ДОЛЖНО БЫЛО ПРОИЗОЙТИ В ПРИНЦИПЕ- " +
            "ЭТО СЕРВИСНОЕ СООБЩЕНИЕ. ЕСЛИ ВЫ ЕГО ВИДИТЕ, СВЯЖИТЕСЬ, ПОЖАЛУЙСТА, СО СЛУЖБОЙ ПОДДЕРЖКИ.</b>";
    private final String WRONG_APP_USER_ROLE_ERROR_MESSAGE = "Вы зарегистрированы, как заведение или как человек, " +
            "отвечающий за компанию в целом- функционал этого чата для Вас не доступен.\n" +
            "Если Вам необходимо зарегистрироваться, как персоналу, попросите организацию, " +
            "зарегистрировавшую Вас, удалить Вас или переназначить руководство на другой аккаунт телеграм.";
    private final String NULL_STATE_ERROR_MESSAGE = "<b>Произошла непредвиденная ошибка. " +
            "Свяжитесь со специалистом технической поддержки для устранения ошибки.</b>";
    private final String NOT_REGISTERED_ERROR_MESSAGE = "Вы не зарегистрированы, как работник компании. " +
            "Обратитесь к Вашему руководителю и предоставьте ему свой телеграмм-id " +
            "для регистрации";
    private final String WELCOME_MESSAGE = "Вы находитесь на главной странице чат-бота.\n" +
            "Вам доступны следующие действия:";
    private final String CATEGORY_CHOICE_MESSAGE = "Меню выбора категории для изучения";
    private final String ITEM_CHOICE_MESSAGE = "Меню выбора блюда";


    private final MessageUtil messageUtil;

    /**
     * Метод получает пользователя и объект update и выдает сообщение с вьюхой, с которой пользователь уходит,
     * чтобы вернуться назад
     */

    public SendMessage previousView(Update update, EmployeeView currentView) {
        var answer = previousViewStrategy(update, currentView);

        return messageUtil.addServiceMessage(answer, PREVIOUS_VIEW_ERROR_MESSAGE);
    }

    /**
     * вместо RESOURCES_CHOICE_VIEW и TEST_CHOICE_VIEW
     * передается WELCOME_VIEW из-за невозможности получить в аргументы мапу из бд
     */
    private SendMessage previousViewStrategy(Update update, EmployeeView currentView) {

        if (currentView == null) {
            return nullCurrentViewErrorView(update);
        }

        switch (currentView) {
            case NULL_STATE_ERROR_VIEW:
                return nullStateErrorView(update);
            case WRONG_APP_USER_ROLE_ERROR_VIEW:
                return wrongAppUserRoleErrorView(update);
            case NOT_REGISTERED_ERROR_VIEW:
                return notRegisteredErrorView(update);
            case WELCOME_VIEW:
                return welcomeView(update);
            case CATEGORY_CHOICE_VIEW:
                return welcomeView(update);
            case CATEGORY_INFO_VIEW:
                return welcomeView(update);
            case TEST_CHOICE_VIEW:
                return welcomeView(update);
            default:
                return previousView(update, currentView);
        }
    }


    /**
     * ЭТО СООБЩЕНИЕ НЕ МОЖЕТ ВЫЛЕТЕТЬ В ПРИНЦИПЕ!!!
     * Сообщение о том, что не задан текущий вью пользователя
     */
    public SendMessage nullCurrentViewErrorView(Update update) {
        var answer = messageUtil.blankAnswer(update);

        return messageUtil.addServiceMessage(answer, NULL_CURRENT_VIEW_ERROR_MESSAGE);
    }

    /**
     * Сообщение об ошибке статуса пользователя в приложении- если он зарегистрирован в другом чате
     * и попробовал воспользоваться функционалом этого
     * EmployeeView.WRONG_APP_USER_ROLE_ERROR_VIEW
     */
    public SendMessage wrongAppUserRoleErrorView(Update update) {
        var answer = messageUtil.blankAnswer(update);

        answer.setText(WRONG_APP_USER_ROLE_ERROR_MESSAGE);

        return answer;
    }

    /**
     * Сообщение об ошибке регистрации- выскакивает, если пользователя вообще нет в базе данных
     * EmployeeView.NOT_REGISTERED_ERROR_VIEW
     */
    public SendMessage notRegisteredErrorView(Update update) {
        var answer = messageUtil.blankAnswer(update);

        answer.setText(NOT_REGISTERED_ERROR_MESSAGE);

        return answer;
    }

    /**
     * Сообщение об ошибке в результате потери статуса
     * ПО ИДЕЕ, ВООБЩЕ НИКОГДА НЕ ДОЛЖНО ВЫСКАКИВАТЬ- ЭТО ПРОВЕРКА СЛУЧАЙНЫХ СИТУАЦИЙ
     * EmployeeView.NULL_STATE_ERROR_VIEW
     */

    public SendMessage nullStateErrorView(Update update) {
        return textMessage(NULL_STATE_ERROR_MESSAGE, update);
    }

    /**
     * Первое приветственное сообщение
     * EmployeeView.WELCOME_VIEW
     */
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
                        button("ИЗУЧЕНИЕ МАТЕРИАЛА", "/category_choice_view"),
                        button("ТЕСТИРОВАНИЕ", "/test_choice_view")
                )
        );

        keyboard.setKeyboard(keyboardRows);

        return keyboard;
    }

    /**
     * Вью выбора ресурса для изучения
     * EmployeeView.CATEGORY_CHOICE_VIEW
     */
    public SendMessage categoryChoiceView(Update update, Map<Long, String> resources) {
        var answer = messageUtil.blankAnswer(update);
        var categoryChoiceViewKeyboardMarkup = resourcesChoiceViewKeyboardMarkup(resources);

        answer.setText(CATEGORY_CHOICE_MESSAGE);
        answer.setReplyMarkup(categoryChoiceViewKeyboardMarkup);

        return answer;
    }

    private InlineKeyboardMarkup resourcesChoiceViewKeyboardMarkup(Map<Long, String> resources) {
        var keyboard = new InlineKeyboardMarkup();
        final List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<>();

        resources.forEach((key, value) -> keyboardRows.add(List.of(
                button(value, String.format("/category_info_%d", key))
        )));
        keyboardRows.add(homepageKeyboardRow());
        keyboard.setKeyboard(keyboardRows);

        return keyboard;
    }

    /**
     * Вью выбора блюд для изучения
     * EmployeeView.ITEM_CHOICE_VIEW
     */
    public SendMessage itemChoiceView(Update update, Map<Long, String> items) {
        var answer = messageUtil.blankAnswer(update);
        var itemChoiceViewKeyboardMarkup = itemChoiceViewKeyboardMarkup(items);

        answer.setText(ITEM_CHOICE_MESSAGE);
        answer.setReplyMarkup(itemChoiceViewKeyboardMarkup);

        return answer;
    }

    private InlineKeyboardMarkup itemChoiceViewKeyboardMarkup(Map<Long, String> items) {
        var keyboard = new InlineKeyboardMarkup();
        var keyboardRows = itemChoiceViewKeyboardMarkupKeyboardFromItemsMap(items);

        keyboardRows.add(homepageKeyboardRow());
        keyboard.setKeyboard(keyboardRows);

        return keyboard;
    }

    private List<List<InlineKeyboardButton>> itemChoiceViewKeyboardMarkupKeyboardFromItemsMap(Map<Long, String> items) {
        final Stack<List<InlineKeyboardButton>> keyboardRows = new Stack<>() {{
            add(new ArrayList<>());
        }};
        final Queue<Map.Entry<Long, String>> temp = new ArrayDeque<>(items.entrySet());
        Map.Entry<Long, String> tempItem;

        while (!temp.isEmpty()) {
            tempItem = temp.poll();
            if (keyboardRows.peek().size() == 2) {
                keyboardRows.add(new ArrayList<>());
            }
            var row = keyboardRows.peek();
            row.add(
                    button(
                            tempItem.getValue(),
                            String.format("/item_choice_%d", tempItem.getKey())
                    )
            );
        }

        return keyboardRows;
    }

    /**
     * Вью просмотра информации по блюдам из категории
     * EmployeeView.CATEGORY_INFO_VIEW
     */

    /**
     * В этом вью сначала идут несколько сообщений с информацией по блюдам, а потом- основное- с кнопками
     * */
    public SendMessage itemInfoView(Update update, String recipe) {
        var answer = messageUtil.blankAnswer(update);

        answer.setText(recipe);
        answer.enableHtml(true);

        return answer;
    }

    public SendMessage categoryInfoView(Update update, String message) {
        var answer = messageUtil.blankAnswer(update);
        var categoryInfoViewKeyboardMarkup = categoryInfoViewKeyboardMarkup();

        answer.setText(message);
        answer.setReplyMarkup(categoryInfoViewKeyboardMarkup);
        answer.enableHtml(true);

        return answer;
    }

    private InlineKeyboardMarkup categoryInfoViewKeyboardMarkup() {
        var keyboard = new InlineKeyboardMarkup();
        var keyboardRows = new ArrayList<List<InlineKeyboardButton>>();

        keyboardRows.add(new ArrayList<>() {{
            add(
                    button(
                            "НАЗАД", "/category_choice_view"
                    )
            );
        }});
        keyboardRows.add(homepageKeyboardRow());
        keyboard.setKeyboard(keyboardRows);

        return keyboard;
    }

    /**
     * UTILS
     * */

    /**
     * Базовое текстовое сообщение
     */
    public SendMessage textMessage(String message, Update update) {
        var chatId = chatId(update);
        SendMessage answer = new SendMessage();
        answer.setChatId(chatId);
        answer.setText(message);
        return answer;
    }

    private long chatId(Update update) {
        return update.hasMessage() ?
                update.getMessage().getChatId() :
                update.getCallbackQuery().getMessage().getChatId();
    }

    private List<InlineKeyboardButton> homepageKeyboardRow() {
        return List.of(
                button("Главная страница", "/start")
        );
    }

    private InlineKeyboardButton button(String text, String callbackData) {
        var button = new InlineKeyboardButton();

        button.setText(text);
        button.setCallbackData(callbackData);

        return button;
    }

    public SendMessage addServiceMessage(SendMessage sendMessage, String serviceMessage) {
        return messageUtil.addServiceMessage(sendMessage, serviceMessage);
    }
}

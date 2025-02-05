package org.klozevitz;

import lombok.AllArgsConstructor;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.enitites.appUsers.Department;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    private final String PREVIOUS_VIEW_ERROR_MESSAGE = "<b>Вы совершили действие, которое привело к остановке " +
            "выполнения процесса. Вернемся к предыдущему экрану:</b>";
    private final String NULL_COMPANY_STATE_ERROR_NOTIFICATION_MESSAGE = "<b>Произошла непредвиденная ошибка. " +
            "Свяжитесь со специалистом технической поддержки для устранения ошибки.</b>";
    private final  String REGISTERED_WELCOME_MESSAGE = "Вы находитесь на главной странице чат-бота.";
    private final String DEPARTMENTS_MANAGEMENT_VIEW_MESSAGE = "На этой странице Вы можете управлять " +
            "Вашими заведениями. Как только Вы добавите первое заведение, оно появится в списке.";
    private final String DEPARTMENT_TELEGRAM_USER_ID_REQUEST_MESSAGE = "Введите id пользователя telegram \n" +
            "(требуется запросить у пользователя числовой идентификатор telegram user id - " +
            "он это может сделать в официальном боте @getmyid_bot) \n\n" +
            "В последствии, если будет введен неправильный id, компанию можно будет легко удалить.";
    private final String DEPARTMENT_REGISTRATION_NOTIFICATION_MESSAGE = "<b>Телеграм-id \"%s\", " +
            "который будет отвечать за новое заведение, зарегистрирован. " +
            "Вы можете отредактировать/удалить его в меню менеджмента заведений.</b>";
    private final String INVALID_DEPARTMENT_TELEGRAM_USER_ID_ERROR_MESSAGE = "Введенная строка не является " +
            "корректным telegramUserId";
    private final String ALREADY_REGISTERED_TELEGRAM_USER_ID_ERROR_MESSAGE = "<b>Введенный Телеграм-id уже " +
            "зарегистрирован в системе</b>";
    private MessageUtil messageUtil;

    /**
     * Метод получает пользователя и объект update и выдает сообщение с вьюхой, с которой пользователь уходит,
     * чтобы вернуться назад
     * */

    public SendMessage previousView(Update update, AppUser appUser) {
        var answer = previousViewStrategy(update, appUser);

        return messageUtil.addServiceMessage(answer, PREVIOUS_VIEW_ERROR_MESSAGE);
    }

    private SendMessage previousViewStrategy(Update update, AppUser currentAppUser) {
        var currentView = currentAppUser.getCompany().getCurrentView();
        var chatId = chatId(update);

        if (currentView == null) {
            return unregisteredWelcomeView(update);
        }

        switch (currentView) {
            case NULL_COMPANY_STATE_NOTIFICATION_VIEW:
                return nullCompanyStateNotificationView(update);
            case UNREGISTERED_WELCOME_VIEW:
                return unregisteredWelcomeView(update);
            case EMAIL_REQUEST_VIEW:
                return emailRequestView(update);
            case EMAIL_CONFIRMATION_REQUEST_VIEW:
                return emailConfirmationRequestView(update);
            case REGISTERED_WELCOME_VIEW:
                return registeredWelcomeView(update);
            case EMAIL_CONFIRMATION_NOTIFICATION_VIEW:
                return emailConfirmationNotificationView(chatId);
            case DEPARTMENTS_MANAGEMENT_VIEW:
                return departmentsManagementView(update, currentAppUser);
            case DEPARTMENT_TELEGRAM_USER_ID_REQUEST_VIEW:
                return departmentTelegramUserIdRequestView(update);
            default:
                return previousView(update, currentAppUser);
        }
    }

    /**
     * Первое приветственное сообщение для незарегистрированных пользователей
     * CompanyView.UNREGISTERED_WELCOME_VIEW
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


    /**
     * Вью запрашивает мейл у пользователя
     * CompanyView.EMAIL_REQUEST_VIEW
     * */
    public SendMessage emailRequestView(Update update) {
        var chatId = chatId(update);

        return textMessage(EMAIL_REQUEST_MESSAGE, chatId);
    }

    /**
     * Вью запрашивает подтверждение мейла пользователя
     * CompanyView.EMAIL_CONFIRMATION_REQUEST_VIEW
     * */
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

    /**
     * Вью уведомляет пользователя о невозможности послать электронную почту
     * и отправляет на вью UNREGISTERED_WELCOME_VIEW
     * СВОЕГО ВЬЮ В CompanyView НЕТ
     * */
    public SendMessage emailCanNotBeSentNotificationView(Update update) {
        var email = update.getMessage().getText();
        var errorMessage = String.format(EMAIL_CANNOT_BE_SENT_MESSAGE, email);
        var answer = unregisteredWelcomeView(update);

        return messageUtil.addServiceMessage(answer, errorMessage);
    }

    /**
     * Вью уведомляет о том, что был введеден некорректный мейл,
     * и отправляет на вью EMAIL_REQUEST_VIEW
     * СВОЕГО ВЬЮ В CompanyView НЕТ
     * */
    public SendMessage wrongEmailNotificationView(Update update) {
        var answer = emailRequestView(update);

        return messageUtil.addServiceMessage(answer, WRONG_EMAIL_NOTIFICATION_MESSAGE);
    }

    /**
     * Вью уведомляет о том, что был введеден уже зарегистрированный мейл,
     * и отправляет на вью EMAIL_REQUEST_VIEW
     * СВОЕГО ВЬЮ В CompanyView НЕТ
     * */
    public SendMessage alreadyRegisteredEmailNotificationView(Update update) {
        var answer = emailRequestView(update);

        return messageUtil.addServiceMessage(answer, ALREADY_REGISTERED_EMAIL_NOTIFICATION_MESSAGE);
    }

    /**
     * Вью уведомляет пользователя об успешном завершении регистрации
     * CompanyView.EMAIL_CONFIRMATION_NOTIFICATION_VIEW
     * */
    public SendMessage emailConfirmationNotificationView(long chatId) {
        var answer = messageUtil.blankAnswer(chatId);
        var emailConfirmationNotificationViewKeyboardMarkup =
                emailConfirmationNotificationViewKeyboardMarkup();

        answer.setText(EMAIL_CONFIRMATION_NOTIFICATION_MESSAGE);
        answer.setReplyMarkup(emailConfirmationNotificationViewKeyboardMarkup);

        return answer;
    }

    private InlineKeyboardMarkup emailConfirmationNotificationViewKeyboardMarkup() {
        var keyboard = new InlineKeyboardMarkup();
        var row = List.of(
                button("ОСНОВНОЙ ЭКРАН", "/start")
        );
        var keyboardRows = List.of(row);
        keyboard.setKeyboard(keyboardRows);
        return keyboard;
    }


    /**
     * Вьюхи для зарегистрированных пользователей
     * */

    /**
     * Первое приветственное сообщение для незарегистрированных пользователей
     * CompanyView.REGISTERED_WELCOME_VIEW
     * */
    public SendMessage registeredWelcomeView(Update update) {
        var answer = messageUtil.blankAnswer(update);
        var registeredWelcomeViewKeyboardMarkup =
                registeredWelcomeViewKeyboardMarkup();

        answer.setText(REGISTERED_WELCOME_MESSAGE);
        answer.setReplyMarkup(registeredWelcomeViewKeyboardMarkup);

        return answer;
    }

    private InlineKeyboardMarkup registeredWelcomeViewKeyboardMarkup() {
        var keyboard = new InlineKeyboardMarkup();
        var row = List.of(
                button("УПРАВЛЕНИЕ\nЗАВЕДЕНИЯМИ", "/departments_management"),
                button("УПРАВЛЕНИЕ\nПРОФИЛЕМ", "/profile_management")
        );
        var keyboardRows = List.of(row);
        keyboard.setKeyboard(keyboardRows);
        return keyboard;
    }

    /**
     * Вью управления департаментами
     * CompanyView.DEPARTMENTS_MANAGEMENT_VIEW
     * */
    public SendMessage departmentsManagementView(Update update, AppUser currentAppUser) {
        var departments = currentAppUser.getCompany().getDepartments();
        var answer = messageUtil.blankAnswer(update);
        var departmentsManagementViewKeyboardMarkUp =
                departmentsManagementViewKeyboardMarkUp(departments);

        answer.setText(DEPARTMENTS_MANAGEMENT_VIEW_MESSAGE);
        answer.setReplyMarkup(departmentsManagementViewKeyboardMarkUp);

        return answer;
    }

    private InlineKeyboardMarkup departmentsManagementViewKeyboardMarkUp(Set<Department> departments) {
        var departmentsManagementViewKeyboardMarkUp = new InlineKeyboardMarkup();
        var addDepartmentRow = List.of(
                button("ДОБАВИТЬ ЗАВЕДЕНИЕ", "/add_department"),
                button("Выход", "/start")
        );

        List<List<InlineKeyboardButton>> departmentManagementViewKeyboardMarkupDepartmentsManagementTable;

        if (!departments.isEmpty()) {
            departmentManagementViewKeyboardMarkupDepartmentsManagementTable =
                    departmentsManagementViewKeyboardMarkUpDepartmentsManagementTable(departments);
            departmentManagementViewKeyboardMarkupDepartmentsManagementTable.add(addDepartmentRow);
        } else {
            departmentManagementViewKeyboardMarkupDepartmentsManagementTable =
                    List.of(addDepartmentRow);
        }

        departmentsManagementViewKeyboardMarkUp.setKeyboard(departmentManagementViewKeyboardMarkupDepartmentsManagementTable);

        return departmentsManagementViewKeyboardMarkUp;
    }

    private List<List<InlineKeyboardButton>> departmentsManagementViewKeyboardMarkUpDepartmentsManagementTable(Set<Department> departments) {
        final List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        departments.forEach(d -> {
            var text = String.format("ЗАВЕДЕНИЕ %d", d.getId());
            List<InlineKeyboardButton> row = List.of(
                    button(text, "/asd"),
                    button("X", "/asdasd")
            );
            rows.add(row);
        });

        return rows;
    }

    /**
     * Вью запроса telegramUserId для добавления департамента
     * CompanyView.DEPARTMENT_TELEGRAM_USER_ID_REQUEST_VIEW
     * */
    public SendMessage departmentTelegramUserIdRequestView(Update update) {
        var answer = messageUtil.blankAnswer(update);

        answer.setText(DEPARTMENT_TELEGRAM_USER_ID_REQUEST_MESSAGE);

        return answer;
    }

    /**
     * Вью уведомляет об успешной регистрации департамента
     * СВОЕГО ВЬЮ в CompanyView нет
     * Возвращает меню управления департаментами
     * CompanyView.DEPARTMENTS_MANAGEMENT_VIEW
     * */
    public SendMessage departmentRegistrationNotificationView(Update update, AppUser currentAppUser) {
        var answer = departmentsManagementView(update, currentAppUser);
        var serviceMessage = String.format(
                DEPARTMENT_REGISTRATION_NOTIFICATION_MESSAGE,
                update.getMessage().getText()
        );

        return  messageUtil.addServiceMessage(answer, serviceMessage);
    }

    /**
     * Вью уведомляет о неверно введенном telegramUserId при регистрации департамента
     * СВОЕГО ВЬЮ в CompanyView нет
     * Возвращает меню управления департаментами
     * CompanyView.DEPARTMENT_TELEGRAM_USER_ID_REQUEST_VIEW
     * */
    public SendMessage invalidDepartmentTelegramUserIdErrorView(Update update) {
        var answer = departmentTelegramUserIdRequestView(update);

        return  messageUtil.addServiceMessage(answer, INVALID_DEPARTMENT_TELEGRAM_USER_ID_ERROR_MESSAGE);
    }

    /**
     * Вью уведомляет о том, что введенный при регистрации департамента telegramUserId уже есть в системе
     * СВОЕГО ВЬЮ в CompanyView нет
     * Возвращает меню управления департаментами
     * CompanyView.DEPARTMENTS_MANAGEMENT_VIEW
     * */
    public SendMessage alreadyRegisteredTelegramUserIdErrorView(Update update, AppUser currentAppUser) {
        var answer = departmentsManagementView(update, currentAppUser);

        return messageUtil.addServiceMessage(answer, ALREADY_REGISTERED_TELEGRAM_USER_ID_ERROR_MESSAGE);
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

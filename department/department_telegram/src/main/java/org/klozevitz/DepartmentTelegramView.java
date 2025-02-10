package org.klozevitz;

import lombok.RequiredArgsConstructor;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.enitites.appUsers.Employee;
import org.klozevitz.enitites.menu.Category;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class DepartmentTelegramView {
    private final String PREVIOUS_VIEW_ERROR_MESSAGE = "<b>Вы совершили действие, которое привело к остановке " +
            "выполнения процесса. Вернемся к предыдущему экрану:</b>";
    private final String NULL_VIEW_ERROR_MESSAGE = "<b>ТАКОГО НЕ ДОЛЖНО БЫЛО ПРОИЗОЙТИ В ПРИНЦИПЕ- " +
            "ЭТО СЕРВИСНОЕ СООБЩЕНИЕ. ЕСЛИ ВЫ ЕГО ВИДИТЕ, СВЯЖИТЕСЬ, ПОЖАЛУЙСТА, СО СЛУЖБОЙ ПОДДЕРЖКИ.</b>";
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
    private final String EMPLOYEES_MANAGEMENT_VIEW_MESSAGE = "На этой странице Вы можете управлять " +
            "Вашим персоналом. Как только Вы добавите первого сотрудника, он появится в списке.";
    private final String TG_USER_ID_REQUEST_VIEW_MESSAGE = "Введите id пользователя telegram \n" +
            "(требуется запросить у пользователя числовой идентификатор telegram user id - " +
            "он это может сделать в официальном боте @getmyid_bot) \n\n" +
            "В последствии, если будет введен неправильный id, пользователя можно будет легко удалить.";
    private final String EMPLOYEE_REGISTRATION_NOTIFICATION_MESSAGE = "<b>Телеграм-id \"%s\", " +
            "нового сотрудника зарегистрирован. " +
            "Вы можете отредактировать/удалить его в меню менеджмента персонала.</b>";
    private final String INVALID_DEPARTMENT_TG_ID_ERROR_MESSAGE = "Введенная строка не является " +
            "корректным telegramUserId";
    private final String ALREADY_REGISTERED_TG_ID_ERROR_MESSAGE = "<b>Введенный Телеграм-id уже " +
            "зарегистрирован в системе</b>";
    private final String RESOURCES_MANAGEMENT_VIEW_MESSAGE = "На этой странице Вы можете управлять " +
            "Вашими методическими материалами. Как только Вы добавите первый, он появится в списке.";
    private final String RESOURCE_REQUEST_VIEW_MESSAGE = "Ожидание excel-документа формата, представленного ниже \n\n" +
            "В последствии, его можно будет легко удалить.";
    private final MessageUtil messageUtil;


    /**
     * Метод получает пользователя и объект update и выдает сообщение с вьюхой, с которой пользователь уходит,
     * чтобы вернуться назад
     * */

    public SendMessage previousView(Update update, AppUser currentAppUser) {
        var answer = previousViewStrategy(update, currentAppUser);

        return messageUtil.addServiceMessage(answer, PREVIOUS_VIEW_ERROR_MESSAGE);
    }

    private SendMessage previousViewStrategy(Update update, AppUser currentAppUser) {
        var currentView = currentAppUser.getDepartment().getCurrentView();

        if (currentView == null) {
            return nullCurrentViewErrorView(update);
        }

        switch (currentView) {
            case NULL_DEPARTMENT_STATE_ERROR_VIEW:
                return nullDepartmentStateErrorView(update);
            case WRONG_APP_USER_ROLE_ERROR_VIEW:
                return wrongAppUserRoleErrorView(update);
            case WELCOME_VIEW:
                return welcomeView(update);
            case EMPLOYEES_MANAGEMENT_VIEW:
                return employeesManagementView(update, currentAppUser);
            case NOT_REGISTERED_DEPARTMENT_ERROR_VIEW:
                return notRegisteredDepartmentErrorView(update);
            default:
                return null;
        }
    }

    /**
     * ЭТО СООБЩЕНИЕ НЕ МОЖЕТ ВЫЛЕТЕТЬ В ПРИНЦИПЕ!!!
     * Сообщение о том, что не задан текущий вью пользователя
     * */
    public SendMessage nullCurrentViewErrorView(Update update) {
        var answer = messageUtil.blankAnswer(update);

        return messageUtil.addServiceMessage(answer, NULL_VIEW_ERROR_MESSAGE);
    }


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
                    button("УПРАВЛЕНИЕ МАТЕРИАЛАМИ", "/resources_management")
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
     * Вью управления департаментами
     * DepartmentView.EMPLOYEES_MANAGEMENT_VIEW
     * */
    public SendMessage employeesManagementView(Update update, AppUser currentAppUser) {
        var employees = currentAppUser.getDepartment().getEmployees();
        var answer = messageUtil.blankAnswer(update);
        var employeesManagementViewKeyboardMarkUp =
                employeesManagementViewKeyboardMarkUp(employees);

        answer.setText(EMPLOYEES_MANAGEMENT_VIEW_MESSAGE);
        answer.setReplyMarkup(employeesManagementViewKeyboardMarkUp);

        return answer;
    }

    private InlineKeyboardMarkup employeesManagementViewKeyboardMarkUp(Set<Employee> employees) {
        var keyboardMarkUp = new InlineKeyboardMarkup();
        var addEmployeeRow = List.of(
                button("ДОБАВИТЬ СОТРУДНИКА", "/add_employee"),
                button("Выход", "/start")
        );

        List<List<InlineKeyboardButton>> departmentsManagementTable;

        if (!employees.isEmpty()) {
            departmentsManagementTable =
                    employeesManagementViewKeyboardMarkUpEmployeeManagementTable(employees);
            departmentsManagementTable.add(addEmployeeRow);
        } else {
            departmentsManagementTable =
                    List.of(addEmployeeRow);
        }

        keyboardMarkUp
                .setKeyboard(
                        departmentsManagementTable
                );

        return keyboardMarkUp;
    }

    private List<List<InlineKeyboardButton>> employeesManagementViewKeyboardMarkUpEmployeeManagementTable(Set<Employee> employees) {
        final List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        employees.forEach(e -> {
            var text = String.format("СОТРУДНИК %d", e.getId());
            List<InlineKeyboardButton> row = List.of(
                    button(text, "/asd"),
                    button("X", "/asdasd")
            );
            rows.add(row);
        });

        return rows;
    }

    /**
     * Вью запроса телеграм-id для регистрации нового рабоника
     * DepartmentView.EMPLOYEE_TG_USER_ID_REQUEST_VIEW
     * */
    public SendMessage employeeTgIdRequestView(Update update) {
        var answer = textMessage(TG_USER_ID_REQUEST_VIEW_MESSAGE, update);
        var keyboard = new InlineKeyboardMarkup();
        var homePageRow = homepageKeyboardRow();

        keyboard.setKeyboard(
                List.of(homePageRow)
        );
        answer.setReplyMarkup(keyboard);

        return answer;
    }

    /**
     * Вью уведомляет об успешной регистрации сотрудника
     * СВОЕГО ВЬЮ в DepartmentView нет
     * Возвращает меню управления департаментами
     * DepartmentView.EMPLOYEES_MANAGEMENT_VIEW
     * */
    public SendMessage newEmployeeRegistrationNotificationView(Update update, AppUser currentAppUser) {
        var answer = employeesManagementView(update, currentAppUser);
        var serviceMessage = String.format(
                EMPLOYEE_REGISTRATION_NOTIFICATION_MESSAGE,
                update.getMessage().getText()
        );

        return messageUtil.addServiceMessage(answer, serviceMessage);
    }

    /**
     * Вью уведомляет о неверно введенном telegramUserId при регистрации сотрудника
     * СВОЕГО ВЬЮ в DepartmentView нет
     * Возвращает меню запроса telegramUserId
     * DepartmentView.EMPLOYEE_TELEGRAM_USER_ID_REQUEST_VIEW
     * */
    public SendMessage invalidEmployeeTgIdErrorView(Update update) {
        var answer = employeeTgIdRequestView(update);

        return messageUtil.addServiceMessage(answer, INVALID_DEPARTMENT_TG_ID_ERROR_MESSAGE);
    }

    /**
     * Вью уведомляет о том, что введенный при регистрации сотрудника telegramUserId уже есть в системе
     * СВОЕГО ВЬЮ в DepartmentView нет
     * Возвращает меню управления персоналом
     * DepartmentView.EMPLOYEES_MANAGEMENT_VIEW
     * */
    public SendMessage alreadyRegisteredTelegramUserIdErrorView(Update update, AppUser currentAppUser) {
        var answer = employeesManagementView(update, currentAppUser);

        return messageUtil.addServiceMessage(answer, ALREADY_REGISTERED_TG_ID_ERROR_MESSAGE);
    }

    /**
     * Вью управления меню
     * DepartmentView.RESOURCES_MANAGEMENT_VIEW
     * */
    public SendMessage resourcesManagementView(Update update, AppUser currentAppUser) {
        var resources = currentAppUser.getDepartment().getMenu();
        var answer = messageUtil.blankAnswer(update);
        var resourcesManagementViewKeyboardMarkUp =
                resourcesManagementViewKeyboardMarkUp(resources);

        answer.setText(RESOURCES_MANAGEMENT_VIEW_MESSAGE);
        answer.setReplyMarkup(resourcesManagementViewKeyboardMarkUp);

        return answer;
    }

    private InlineKeyboardMarkup resourcesManagementViewKeyboardMarkUp(Set<Category> resources) {
        var keyboardMarkUp = new InlineKeyboardMarkup();
        var addEmployeeRow = List.of(
                button("ДОБАВИТЬ КАТЕГОРИЮ МЕНЮ", "/add_category"),
                button("Выход", "/start")
        );

        List<List<InlineKeyboardButton>> resourcesManagementTable;

        if (!resources.isEmpty()) {
            resourcesManagementTable =
                    resourcesManagementViewKeyboardMarkUpResourcesManagementTable(resources);
            resourcesManagementTable.add(addEmployeeRow);
        } else {
            resourcesManagementTable =
                    List.of(addEmployeeRow);
        }

        keyboardMarkUp
                .setKeyboard(
                        resourcesManagementTable
                );

        return keyboardMarkUp;
    }

    private List<List<InlineKeyboardButton>> resourcesManagementViewKeyboardMarkUpResourcesManagementTable(Set<Category> resources) {
        final List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        resources.forEach(e -> {
            var text = String.format("Меню %d", e.getId());
            List<InlineKeyboardButton> row = List.of(
                    button(text, "/asd"),
                    button("X", "/asdasd")
            );
            rows.add(row);
        });

        return rows;
    }

    /**
     * Вью запроса телеграм-id для регистрации нового рабоника
     * DepartmentView.EMPLOYEE_TG_USER_ID_REQUEST_VIEW
     * */
    public SendMessage resourceRequestView(Update update) {
        var answer = textMessage(RESOURCE_REQUEST_VIEW_MESSAGE, update);
        var keyboard = new InlineKeyboardMarkup();
        var homePageRow = homepageKeyboardRow();

        keyboard.setKeyboard(
                List.of(homePageRow)
        );
        answer.setReplyMarkup(keyboard);

        return answer;
    }


    /**
     * Базовое текстовое сообщение
     * */
    public SendMessage textMessage(String message, Update update) {
        var chatId = chatId(update);
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
        return textMessage(NULL_DEPARTMENT_STATE_ERROR_MESSAGE, update);
    }

    /**
     * UTILS
     * */

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
}

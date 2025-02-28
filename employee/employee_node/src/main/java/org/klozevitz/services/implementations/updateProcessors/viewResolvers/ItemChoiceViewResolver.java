package org.klozevitz.services.implementations.updateProcessors.viewResolvers;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.EmployeeTelegramView;
import org.klozevitz.enitites.appUsers.Employee;
import org.klozevitz.enitites.menu.Item;
import org.klozevitz.enitites.menu.resources.WorkBook;
import org.klozevitz.repositories.appUsers.EmployeeRepo;
import org.klozevitz.repositories.menu.ItemRepo;
import org.klozevitz.services.interfaces.updateProcessors.UpdateProcessor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.klozevitz.enitites.appUsers.enums.views.EmployeeView.ITEM_CHOICE_VIEW;

@Log4j
@RequiredArgsConstructor
public class ItemChoiceViewResolver implements UpdateProcessor<Update, Long> {
    private final String WRONG_CATEGORY_ID_ERROR_MESSAGE = "<b>Вы перенаправлены на предыдущую страницу. " +
            "Запрашиваемая Вами категория меню не найдена</b>";
    private final String WRONG_COMMAND_ERROR_MESSAGE = "Вы совершили некорректное действие- " +
            "возвращаетесь на предыдущий экран";

    private final EmployeeRepo employeeRepo;
    private final ItemRepo itemRepo;
    private final EmployeeTelegramView telegramView;
    private final UpdateProcessor<Update, Long> categoryChoiceViewResolver;


    // TODO не доделано
    @Override
    public SendMessage processUpdate(Update update, Long telegramUserId) {
        return update.hasMessage() ?
                commandUpdateResolver(update, telegramUserId) :
                callbackQueryUpdateResolver(update, telegramUserId);
    }

    private SendMessage commandUpdateResolver(Update update, long telegramUserId) {
        var persistentEmployee = employeeRepo.findByAppUserTelegramUserId(telegramUserId);

        // TODO вернуть список категорий
//        return telegramView.itemChoiceView(update, itemForView);
        return null;
    }

    private SendMessage callbackQueryUpdateResolver(Update update, long telegramUserId) {
        var data = update.getCallbackQuery().getData();
        var categoryId = categoryIdFromUpdate(data);

        if (categoryId == -1) {
            return wrongCategoryIdView(update, telegramUserId, data);
        }

        List<Item> items = itemRepo.findByCategoryId(categoryId);
        Map<Long, String> mapForView = items.stream()
                .collect(Collectors.toMap(
                        Item::getId,
                        Item::getName
                ));

        employeeRepo.setEmployeeCurrentView(ITEM_CHOICE_VIEW.name(), telegramUserId);

        return telegramView.itemChoiceView(update, mapForView);
    }

    private WorkBook setNewWorkbookToEmployee(long categoryId, Employee persistentEmployee) {
        WorkBook workBook;
        var menu = itemRepo.findByCategoryId(categoryId);
        workBook = new WorkBook(new HashSet<>(menu));

        persistentEmployee.setWorkbook(workBook);
        employeeRepo.save(persistentEmployee);
        return workBook;
    }

    /**
     * такого вью нет.
     * выдаем сообщение об ошибке и отправляем на предыдущий вью -
     * EmployeeView.RESOURCES_CHOICE_VIEW
     */
    private SendMessage wrongCategoryIdView(Update update, Long telegramUserId, String data) {
        log.error(String.format("юзер с telegramUserId: %d не получил категорию с id из апдейта %s",
                telegramUserId,
                data)
        );

        var answer = categoryChoiceViewResolver.processUpdate(update, telegramUserId);

        return telegramView.addServiceMessage(answer, WRONG_CATEGORY_ID_ERROR_MESSAGE);
    }

    private long categoryIdFromUpdate(String data) {
        var temp = List.of(data.split("_"));
        var categoryIdInStringFromUpdate = temp.get(temp.size() - 1);

        try {
            return Long.parseLong(categoryIdInStringFromUpdate);
        } catch (Exception e) {
            return -1L;
        }
    }
}

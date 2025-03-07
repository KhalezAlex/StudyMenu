package org.klozevitz.services.implementations.updateProcessors.viewResolvers;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.EmployeeTelegramView;
import org.klozevitz.enitites.menu.Item;
import org.klozevitz.repositories.appUsers.EmployeeRepo;
import org.klozevitz.repositories.menu.ItemRepo;
import org.klozevitz.services.interfaces.updateProcessors.UpdateProcessor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

import static org.klozevitz.enitites.appUsers.enums.views.EmployeeView.CATEGORY_INFO_VIEW;

@Log4j
@RequiredArgsConstructor
public class CategoryIngoViewResolver_ALT implements UpdateProcessor {
    private final String WRONG_CATEGORY_ID_ERROR_MESSAGE = "<b>Вы перенаправлены на предыдущую страницу. " +
            "Запрашиваемая Вами категория меню не найдена</b>";

    private final EmployeeRepo employeeRepo;
    private final ItemRepo itemRepo;
    private final EmployeeTelegramView telegramView;
    private final UpdateProcessor categoryInfoChoiceViewResolver;

    @Override
    public SendMessage processUpdate(Update update) {
        var telegramUserId = telegramUserId(update);
        var callBackData = command(update);
        var categoryId = categoryIdFromUpdate(callBackData);

        if (categoryId == -1) {
            return wrongCategoryIdView(update);
        }

        employeeRepo.setEmployeeCurrentView(CATEGORY_INFO_VIEW.name(), telegramUserId);

        var items = new ArrayList<>(itemRepo.findByCategoryId(categoryId));
        Item tempItem;
        while(items.size() > 1) {
            tempItem = items.get(0);
            // послать сообщение
            //
            items.remove(0);
            // понадобится костыль- boolean - чтобы понимать, удалять ли список сообщений
        }

        // вернуть последнее сообщение
        return null;
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

    /**
     * такого вью нет.
     * выдаем сообщение об ошибке и отправляем на предыдущий вью -
     * EmployeeView.CATEGORY_INFO_CHOICE_VIEW
     */
    private SendMessage wrongCategoryIdView(Update update) {
        var telegramUserId = telegramUserId(update);
        var data = command(update);

        log.error(String.format("юзер с telegramUserId: %d не получил категорию с id из апдейта %s",
                telegramUserId,
                data)
        );

        var answer = categoryInfoChoiceViewResolver.processUpdate(update);

        return telegramView.addServiceMessage(answer, WRONG_CATEGORY_ID_ERROR_MESSAGE);
    }
}

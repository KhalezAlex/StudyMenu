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

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.klozevitz.enitites.appUsers.enums.views.EmployeeView.CATEGORY_INFO_VIEW;

@Log4j
@RequiredArgsConstructor
public class CategoryInfoViewResolver implements UpdateProcessor {
    private final String WRONG_CATEGORY_ID_ERROR_MESSAGE = "<b>Вы перенаправлены на предыдущую страницу. " +
            "Запрашиваемая Вами категория меню не найдена</b>";

    private final EmployeeRepo employeeRepo;
    private final ItemRepo itemRepo;
    private final EmployeeTelegramView telegramView;
    private final UpdateProcessor categoryInfoChoiceViewResolver;


    @Override
    public SendMessage processUpdate(Update update) {
        var telegramUserId = telegramUserId(update);
        var data = update.getCallbackQuery().getData();
        var categoryId = categoryIdFromUpdate(data);

        if (categoryId == -1) {
            return wrongCategoryIdView(update, telegramUserId, data);
        }

        var items = itemRepo.findByCategoryId(categoryId);

        employeeRepo.setEmployeeCurrentView(CATEGORY_INFO_VIEW.name(), telegramUserId);

        var message = categoryInfoMessage(items);

        return telegramView.categoryInfoView(update, message);
    }

    private String categoryInfoMessage(List<Item> items) {
        var sb = new StringBuilder();

        items.forEach(item -> {
            var recipe = recipeFromBaseEntity(item);

            sb
                    .append(recipe)
                    .append("\n\n");
        });

        var maxCharCount = new AtomicInteger(0);
        var strings = List.of(sb.toString().split("\n"));

        strings.forEach(string -> {
            if (string.length() > maxCharCount.get()) {
                maxCharCount.set(string.length());
            }
        });

        var line = "\n\n" + String
                .valueOf('=')
                .repeat((int) (maxCharCount.get() * 0.7)) + "\n\n";
        var result = sb
                .toString()
                .replace("\n\n", line)
                .replace("</b>", "</b>\n");

        return line + result;
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

        var answer = categoryInfoChoiceViewResolver.processUpdate(update);

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

    private String recipeFromBaseEntity(Item item) {
        AtomicInteger atomicIndex = new AtomicInteger(1);
        StringBuilder sb = new StringBuilder();

        sb
                .append("<b>")
                .append(item.getName())
                .append("</b>");

        item.getIngredients().forEach(
                ingredient -> sb
                        .append("\n  ")
                        .append(atomicIndex.getAndIncrement())
                        .append(".   ")
                        .append(ingredient.getName())
                        .append(" - ")
                        .append(ingredient.getWeight().intValue())
                        .append(ingredient.getUnits()));

        return sb.toString();
    }
}

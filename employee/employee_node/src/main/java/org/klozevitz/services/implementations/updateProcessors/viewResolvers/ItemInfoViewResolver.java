package org.klozevitz.services.implementations.updateProcessors.viewResolvers;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.EmployeeTelegramView;
import org.klozevitz.enitites.appUsers.enums.views.EmployeeView;
import org.klozevitz.enitites.menu.Item;
import org.klozevitz.repositories.appUsers.EmployeeRepo;
import org.klozevitz.repositories.menu.ItemRepo;
import org.klozevitz.services.interfaces.updateProcessors.UpdateProcessor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.concurrent.atomic.AtomicInteger;

@Log4j
@RequiredArgsConstructor
public class ItemInfoViewResolver implements UpdateProcessor<Update, Long> {
    private final EmployeeRepo employeeRepo;
    private final ItemRepo itemRepo;
    private final EmployeeTelegramView telegramView;
    private final UpdateProcessor<Update, EmployeeView> previousViewUpdateProcessor;


    // TODO разобраться, почему сюда приходит tguserid
    @Override
    public SendMessage processUpdate(Update update, Long id) {
        var command = update.getCallbackQuery().getData();
        var itemId = Long.parseLong(command.substring(command.lastIndexOf('_') + 1));
        var optionalPersistentItem = itemRepo.findById(itemId);
        var telegramUserId = telegramUserId(update);
        var employee = employeeRepo.findByAppUserTelegramUserId(telegramUserId);

        if (optionalPersistentItem.isEmpty()) {
            return previousViewUpdateProcessor.processUpdate(update, employee.getCurrentView());
        }

        var categoryId = optionalPersistentItem.get().getCategory().getId();
        var itemRecipe = itemStringFromBaseEntity(optionalPersistentItem.get());

        return telegramView.itemInfoView(update, itemRecipe, categoryId);
    }

    private String itemStringFromBaseEntity(Item item) {
        AtomicInteger atomicIndex = new AtomicInteger(1);
        StringBuilder sb = new StringBuilder();

        sb
                .append("<b>")
                .append(item.getName())
                .append("</b>")
                .append("\n");

        item.getIngredients().forEach(
                ingredient -> sb
                        .append("\n  ")
                        .append(atomicIndex.getAndIncrement())
                        .append(". ")
                        .append(ingredient.getName())
                        .append(" - ")
                        .append(ingredient.getWeight())
                        .append(ingredient.getUnits()));

        return sb.toString();
    }

    private long telegramUserId(Update update) {
        return update.hasMessage() ?
                update.getMessage().getFrom().getId() :
                update.getCallbackQuery().getFrom().getId();
    }
}

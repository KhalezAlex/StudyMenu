package org.klozevitz.services.implementations.updateProcessors.viewResolvers;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.EmployeeTelegramView;
import org.klozevitz.enitites.menu.Category;
import org.klozevitz.repositories.appUsers.EmployeeRepo;
import org.klozevitz.repositories.menu.CategoryRepo;
import org.klozevitz.services.interfaces.updateProcessors.UpdateProcessor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;
import java.util.stream.Collectors;

import static org.klozevitz.enitites.appUsers.enums.views.EmployeeView.CATEGORY_TEST_CHOICE_VIEW;

@Log4j
@RequiredArgsConstructor
public class CategoryTestChoiceViewResolver implements UpdateProcessor<Update, Long> {
    private final EmployeeRepo employeeRepo;
    private final CategoryRepo categoryRepo;
    private final EmployeeTelegramView telegramView;

    @Override
    public SendMessage processUpdate(Update update, Long telegramUserId) {
        var resources = resources(telegramUserId);

        employeeRepo.setEmployeeCurrentView(CATEGORY_TEST_CHOICE_VIEW.name(), telegramUserId);

        return telegramView.categoryTestChoiceView(update, resources);
    }

    private Map<Long, String> resources(long telegramUserId) {
        return categoryRepo
                .categoriesByEmployeeTelegramUserId(telegramUserId)
                .stream()
                .collect(
                        Collectors.toMap(Category::getId, Category::getName)
                );
    }
}

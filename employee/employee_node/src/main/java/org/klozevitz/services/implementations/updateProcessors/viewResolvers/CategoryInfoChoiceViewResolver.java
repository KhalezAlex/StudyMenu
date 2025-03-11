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

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

import static org.klozevitz.enitites.appUsers.enums.views.EmployeeView.CATEGORY_INFO_CHOICE_VIEW;

@Log4j
@RequiredArgsConstructor
public class CategoryInfoChoiceViewResolver implements UpdateProcessor {
    private final EmployeeRepo employeeRepo;
    private final CategoryRepo categoryRepo;
    private final EmployeeTelegramView telegramView;

    @Override
    public ArrayList<SendMessage> processUpdate(Update update) {
        var telegramUserId = telegramUserId(update);
        var resources = resources(telegramUserId);
        ArrayList<SendMessage> answer = new ArrayList<>();

        employeeRepo.setEmployeeCurrentView(CATEGORY_INFO_CHOICE_VIEW.name(), telegramUserId);

        answer.add(
                telegramView.categoryInfoChoiceView(update, resources)
        );

        return answer;
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

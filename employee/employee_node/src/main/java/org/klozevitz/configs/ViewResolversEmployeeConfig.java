package org.klozevitz.configs;

import lombok.RequiredArgsConstructor;
import org.klozevitz.EmployeeTelegramView;
import org.klozevitz.repositories.appUsers.EmployeeRepo;
import org.klozevitz.repositories.menu.CategoryRepo;
import org.klozevitz.repositories.menu.ItemRepo;
import org.klozevitz.services.implementations.updateProcessors.viewResolvers.ItemChoiceViewResolver;
import org.klozevitz.services.implementations.updateProcessors.viewResolvers.CategoryChoiceViewResolver;
import org.klozevitz.services.implementations.updateProcessors.viewResolvers.ItemInfoViewResolver;
import org.klozevitz.services.implementations.updateProcessors.viewResolvers.WelcomeViewResolver;
import org.klozevitz.services.interfaces.updateProcessors.UpdateProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.objects.Update;

@Configuration
@RequiredArgsConstructor
public class ViewResolversEmployeeConfig {
    private final ApplicationContext appContext;
    private final EmployeeRepo employeeRepo;
    private final CategoryRepo categoryRepo;
    private final ItemRepo itemRepo;

    @Bean(name = "welcomeViewResolver")
    public UpdateProcessor<Update, Long> welcomeViewResolver() {
        return new WelcomeViewResolver(
                employeeRepo,
                appContext.getBean("telegramView", EmployeeTelegramView.class)
        );
    }

    @Bean(name = "categoryChoiceViewResolver")
    public UpdateProcessor<Update, Long> categoryChoiceViewResolver() {
        return new CategoryChoiceViewResolver(
                employeeRepo,
                categoryRepo,
                appContext.getBean("telegramView", EmployeeTelegramView.class)
        );
    }

    @Bean(name = "itemChoiceViewResolver")
    public UpdateProcessor<Update, Long> itemChoiceViewResolver() {
        return new ItemChoiceViewResolver(
                employeeRepo,
                itemRepo,
                appContext.getBean("telegramView", EmployeeTelegramView.class),
                categoryChoiceViewResolver()
        );
    }

    @Bean(name = "itemInfoViewResolver")
    public UpdateProcessor<Update, Long> itemViewResolver() {
        return new ItemInfoViewResolver(
                employeeRepo,
                itemRepo,
                appContext.getBean("telegramView", EmployeeTelegramView.class),
                appContext.getBean("previousViewUpdateProcessor", UpdateProcessor.class)
                );
    }
}

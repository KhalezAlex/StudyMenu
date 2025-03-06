package org.klozevitz.configs._legacy.viewResolvers;

import lombok.RequiredArgsConstructor;
import org.klozevitz.EmployeeTelegramView;
import org.klozevitz.MessageUtil;
import org.klozevitz.repositories.appUsers.EmployeeRepo;
import org.klozevitz.repositories.menu.CategoryRepo;
import org.klozevitz.repositories.menu.ItemRepo;
import org.klozevitz.services.implementations.updateProcessors_LEGACY.viewResolvers.CategoryInfoViewResolver;
import org.klozevitz.services.implementations.updateProcessors_LEGACY.viewResolvers.CategoryChoiceViewResolver;
import org.klozevitz.services.implementations.updateProcessors_LEGACY.viewResolvers.CategoryTestChoiceViewResolver;
import org.klozevitz.services.implementations.updateProcessors_LEGACY.viewResolvers.WelcomeViewResolver;
import org.klozevitz.services.interfaces.main.AnswerProducer;
import org.klozevitz.services.interfaces.updateProcessors.UpdateProcessor_LEGACY;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.objects.Update;

@Configuration
@RequiredArgsConstructor
public class ViewResolversEmployeeConfig_LEGACY {
    private final ApplicationContext appContext;
    private final AnswerProducer answerProducer;
    private final EmployeeRepo employeeRepo;
    private final CategoryRepo categoryRepo;
    private final ItemRepo itemRepo;
    private MessageUtil messageUtil() {
        return new MessageUtil();
    }

    @Bean("telegramView")
    public EmployeeTelegramView telegramView() {
        return new EmployeeTelegramView(
                messageUtil()
        );
    }

    @Bean(name = "welcomeViewResolver")
    public UpdateProcessor_LEGACY<Update, Long> welcomeViewResolver() {
        return new WelcomeViewResolver(
                employeeRepo,
                appContext.getBean("telegramView", EmployeeTelegramView.class)
        );
    }

    @Bean(name = "categoryChoiceViewResolver")
    public UpdateProcessor_LEGACY<Update, Long> categoryChoiceViewResolver() {
        return new CategoryChoiceViewResolver(
                employeeRepo,
                categoryRepo,
                appContext.getBean("telegramView", EmployeeTelegramView.class)
        );
    }

    @Bean(name = "categoryTestChoiceViewResolver")
    public UpdateProcessor_LEGACY<Update, Long> categoryTestChoiceViewResolver() {
        return new CategoryTestChoiceViewResolver(
                employeeRepo,
                categoryRepo,
                appContext.getBean("telegramView", EmployeeTelegramView.class)
        );
    }

    @Bean(name = "categoryInfoViewResolver")
    public UpdateProcessor_LEGACY<Update, Long> itemChoiceViewResolver() {
        return new CategoryInfoViewResolver(
                employeeRepo,
                itemRepo,
                appContext.getBean("telegramView", EmployeeTelegramView.class),
                categoryChoiceViewResolver()
        );
    }
}

package org.klozevitz.configs.viewResolvers;

import lombok.RequiredArgsConstructor;
import org.klozevitz.EmployeeTelegramView;
import org.klozevitz.MessageUtil;
import org.klozevitz.repositories.appUsers.EmployeeRepo;
import org.klozevitz.repositories.menu.CategoryRepo;
import org.klozevitz.repositories.menu.ItemRepo;
import org.klozevitz.services.implementations.updateProcessors.viewResolvers.CategoryInfoChoiceViewResolver;
import org.klozevitz.services.implementations.updateProcessors.viewResolvers.CategoryInfoViewResolver;
import org.klozevitz.services.implementations.updateProcessors.viewResolvers.CategoryTestChoiceViewResolver;
import org.klozevitz.services.implementations.updateProcessors.viewResolvers.WelcomeViewResolver;
import org.klozevitz.services.interfaces.main.AnswerProducer;
import org.klozevitz.services.interfaces.updateProcessors.UpdateProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ViewResolversEmployeeConfig {
    private final ApplicationContext appContext;
    private final AnswerProducer answerProducer;
    private final EmployeeRepo employeeRepo;
    private final CategoryRepo categoryRepo;
    private final ItemRepo itemRepo;

    @Bean(name = "messageUtil")
    public MessageUtil messageUtil() {
        return new MessageUtil();
    }

    @Bean("telegramView")
    public EmployeeTelegramView telegramView() {
        return new EmployeeTelegramView(
                messageUtil()
        );
    }

    @Bean(name = "welcomeViewResolver")
    public UpdateProcessor welcomeViewResolver() {
        return new WelcomeViewResolver(
                employeeRepo,
                appContext.getBean("telegramView", EmployeeTelegramView.class)
        );
    }

    @Bean(name = "categoryInfoChoiceViewResolver")
    public UpdateProcessor categoryInfoChoiceViewResolver() {
        return new CategoryInfoChoiceViewResolver(
                employeeRepo,
                categoryRepo,
                appContext.getBean("telegramView", EmployeeTelegramView.class)
        );
    }

    @Bean(name = "categoryTestChoiceViewResolver")
    public UpdateProcessor categoryTestChoiceViewResolver() {
        return new CategoryTestChoiceViewResolver(
                employeeRepo,
                categoryRepo,
                appContext.getBean("telegramView", EmployeeTelegramView.class)
        );
    }

    @Bean(name = "categoryInfoViewResolver")
    public UpdateProcessor categoryInfoViewResolver() {
        return new CategoryInfoViewResolver(
                employeeRepo,
                itemRepo,
                appContext.getBean("telegramView", EmployeeTelegramView.class),
                categoryInfoChoiceViewResolver()
        );
    }
}

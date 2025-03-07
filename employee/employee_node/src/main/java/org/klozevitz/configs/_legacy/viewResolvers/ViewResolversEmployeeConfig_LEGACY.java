package org.klozevitz.configs._legacy.viewResolvers;

import lombok.RequiredArgsConstructor;
import org.klozevitz.EmployeeTelegramView;
import org.klozevitz.MessageUtil;
import org.klozevitz.repositories.appUsers.EmployeeRepo;
import org.klozevitz.repositories.menu.CategoryRepo;
import org.klozevitz.repositories.menu.ItemRepo;
import org.klozevitz.services.implementations.updateProcessors_LEGACY.viewResolvers.CategoryInfoViewResolver_LEGACY;
import org.klozevitz.services.implementations.updateProcessors_LEGACY.viewResolvers.CategoryChoiceViewResolver_LEGACY;
import org.klozevitz.services.implementations.updateProcessors_LEGACY.viewResolvers.CategoryTestChoiceViewResolver_LEGACY;
import org.klozevitz.services.implementations.updateProcessors_LEGACY.viewResolvers.WelcomeViewResolver_LEGACY;
import org.klozevitz.services.interfaces.main.AnswerProducer;
import org.klozevitz.services.interfaces.updateProcessors.UpdateProcessor_LEGACY;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.objects.Update;

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
        return new WelcomeViewResolver_LEGACY(
                employeeRepo,
                appContext.getBean("telegramView", EmployeeTelegramView.class)
        );
    }

    @Bean(name = "categoryChoiceViewResolver")
    public UpdateProcessor_LEGACY<Update, Long> categoryChoiceViewResolver() {
        return new CategoryChoiceViewResolver_LEGACY(
                employeeRepo,
                categoryRepo,
                appContext.getBean("telegramView", EmployeeTelegramView.class)
        );
    }

    @Bean(name = "categoryTestChoiceViewResolver")
    public UpdateProcessor_LEGACY<Update, Long> categoryTestChoiceViewResolver() {
        return new CategoryTestChoiceViewResolver_LEGACY(
                employeeRepo,
                categoryRepo,
                appContext.getBean("telegramView", EmployeeTelegramView.class)
        );
    }

//    @Bean(name = "categoryInfoViewResolver")
//    public UpdateProcessor_LEGACY<Update, Long> itemChoiceViewResolver() {
//        return new CategoryInfoViewResolver_LEGACY(
//                employeeRepo,
//                itemRepo,
//                appContext.getBean("telegramView", EmployeeTelegramView.class),
//                categoryChoiceViewResolver()
//        );
//    }
}

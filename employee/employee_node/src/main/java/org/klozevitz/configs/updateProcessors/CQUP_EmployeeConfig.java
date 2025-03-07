package org.klozevitz.configs.updateProcessors;

import lombok.RequiredArgsConstructor;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.services.implementations.updateProcessors.callbackQuery.CallbackQueryEmployeeUP;
import org.klozevitz.services.implementations.updateProcessors.callbackQuery.byView.CategoryInfoChoiceViewEmployeeCQUP;
import org.klozevitz.services.implementations.updateProcessors.callbackQuery.byView.CategoryInfoViewEmployeeCQUP;
import org.klozevitz.services.implementations.updateProcessors.callbackQuery.byView.CategoryTestChoiceViewEmployeeCQUP;
import org.klozevitz.services.implementations.updateProcessors.callbackQuery.byView.WelcomeViewEmployeeCQUP;
import org.klozevitz.services.interfaces.updateProcessors.UpdateProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class CQUP_EmployeeConfig {
    private final ApplicationContext appContext;
    private final AppUserRepo appUserRepo;

    @Bean(name = "callbackQueryEmployeeUpdateProcessor")
    public UpdateProcessor callbackQueryEmployeeUpdateProcessor() {
        return new CallbackQueryEmployeeUP(
                appUserRepo,
                appContext.getBean("callbackQueryCurrentMessageDispatcher", Map.class),
                appContext.getBean("notRegisteredAppUserUpdateProcessor", UpdateProcessor.class),
                appContext.getBean("previousViewUpdateProcessor", UpdateProcessor.class)
        );
    }

    @Bean(name = "welcomeViewCallbackQueryUpdateProcessor")
    public UpdateProcessor welcomeViewCallbackQueryUpdateProcessor() {
        return new WelcomeViewEmployeeCQUP(
                appUserRepo,
                appContext.getBean("previousViewUpdateProcessor", UpdateProcessor.class),
                appContext.getBean("categoryInfoChoiceViewResolver", UpdateProcessor.class),
                appContext.getBean("categoryTestChoiceViewResolver", UpdateProcessor.class)
        );
    }

    @Bean(name = "categoryInfoChoiceViewCallbackQueryUpdateProcessor")
    public UpdateProcessor categoryInfoChoiceViewCallbackQueryUpdateProcessor() {
        return new CategoryInfoChoiceViewEmployeeCQUP(
                appUserRepo,
                appContext.getBean("previousViewUpdateProcessor", UpdateProcessor.class),
                appContext.getBean("welcomeViewResolver", UpdateProcessor.class),
                appContext.getBean("categoryInfoViewResolver", UpdateProcessor.class)

        );
    }

    @Bean(name = "categoryInfoViewCallbackQueryUpdateProcessor")
    public UpdateProcessor categoryInfoViewCallbackQueryUpdateProcessor() {
        return new CategoryInfoViewEmployeeCQUP(
                appUserRepo,
                appContext.getBean("previousViewUpdateProcessor", UpdateProcessor.class),
                appContext.getBean("welcomeViewResolver", UpdateProcessor.class),
                appContext.getBean("categoryInfoChoiceViewCallbackQueryUpdateProcessor", UpdateProcessor.class)
        );
    }

    @Bean(name = "categoryTestChoiceViewCallbackQueryUpdateProcessor")
    public UpdateProcessor categoryTestChoiceViewCallbackQueryUpdateProcessor() {
        return new CategoryTestChoiceViewEmployeeCQUP(
                appUserRepo,
                appContext.getBean("previousViewUpdateProcessor", UpdateProcessor.class),
                appContext.getBean("welcomeViewResolver", UpdateProcessor.class)
        );
    }
}

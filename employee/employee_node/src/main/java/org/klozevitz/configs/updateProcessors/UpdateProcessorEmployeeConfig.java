package org.klozevitz.configs.updateProcessors;

import lombok.RequiredArgsConstructor;
import org.klozevitz.EmployeeTelegramView;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.repositories.appUsers.EmployeeRepo;
import org.klozevitz.services.implementations.main.MainService;
import org.klozevitz.services.implementations.updateProcessors.util.*;
import org.klozevitz.services.interfaces.main.AnswerProducer;
import org.klozevitz.services.interfaces.main.Main;
import org.klozevitz.services.interfaces.updateProcessors.UpdateProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class UpdateProcessorEmployeeConfig {
    private final ApplicationContext appContext;
    private final AppUserRepo appUserRepo;
    private final EmployeeRepo employeeRepo;
    private final AnswerProducer answerProducer;

    /**
     * Main
     * */
    @Bean
    public Main main() {
        return new MainService(
                appUserRepo,
                answerProducer,
                appContext.getBean("wrongAppUserRoleUpdateProcessor", UpdateProcessor.class),
                appContext.getBean("notRegisteredAppUserUpdateProcessor", UpdateProcessor.class),
                appContext.getBean("commandEmployeeUpdateProcessor", UpdateProcessor.class),
                appContext.getBean("callbackQueryEmployeeUpdateProcessor", UpdateProcessor.class)
        );
    }

    @Bean(name = "wrongAppUserRoleUpdateProcessor")
    public UpdateProcessor wrongAppUserRoleUpdateProcessor() {
        return new WrongAppUserRoleEmployeeUP(
                appContext.getBean("telegramView", EmployeeTelegramView.class)
        );
    }

    @Bean(name = "notRegisteredAppUserUpdateProcessor")
    public UpdateProcessor notRegisteredAppUserUpdateProcessor() {
        return new NotRegisteredAppUserEmployeeUP(
                appContext.getBean("telegramView", EmployeeTelegramView.class)
        );
    }

    @Bean(name = "nullableStateUpdateProcessor")
    public UpdateProcessor nullableStateUpdateProcessor() {
        return new NullableStateEmployeeUP(
                employeeRepo,
                appContext.getBean("telegramView", EmployeeTelegramView.class)
        );
    }

    @Bean(name = "previousViewUpdateProcessor")
    public UpdateProcessor previousViewUpdateProcessor() {
        return new PreviousViewEmployeeUP(
                appUserRepo,
                appContext.getBean("telegramView", EmployeeTelegramView.class)
        );
    }

    @Bean(name = "nullableLastMessageUpdateProcessor")
    public UpdateProcessor nullableLastMessageUpdateProcessor() {
        return new NullableLastMessageEmployeeUP(
                appUserRepo,
                appContext.getBean("telegramView", EmployeeTelegramView.class)
        );
    }

}

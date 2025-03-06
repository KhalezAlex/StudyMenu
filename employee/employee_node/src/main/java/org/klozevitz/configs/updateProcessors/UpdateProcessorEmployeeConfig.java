package org.klozevitz.configs.updateProcessors;

import lombok.RequiredArgsConstructor;
import org.klozevitz.EmployeeTelegramView;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.repositories.appUsers.EmployeeRepo;
import org.klozevitz.services.implementations.main.MainService;
import org.klozevitz.services.implementations.updateProcessors.util.NotRegisteredAppUserEmployeeUP;
import org.klozevitz.services.implementations.updateProcessors.util.NullableStateEmployeeUP;
import org.klozevitz.services.implementations.updateProcessors.util.PreviousViewEmployeeUP;
import org.klozevitz.services.implementations.updateProcessors.util.WrongAppUserRoleEmployeeUP;
import org.klozevitz.services.interfaces.main.AnswerProducer;
import org.klozevitz.services.interfaces.main.Main;
import org.klozevitz.services.interfaces.updateProcessors.UpdateProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.telegram.telegrambots.meta.api.objects.Update;

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

        );
    }

    @Bean(name = "wrongAppUserRoleUpdateProcessor")
    public UpdateProcessor<Update> wrongAppUserRoleUpdateProcessor() {
        return new WrongAppUserRoleEmployeeUP(
                appContext.getBean("telegramView", EmployeeTelegramView.class)
        );
    }

    @Bean(name = "notRegisteredAppUserUpdateProcessor")
    public UpdateProcessor<Update> notRegisteredAppUserUpdateProcessor() {
        return new NotRegisteredAppUserEmployeeUP(
                appContext.getBean("telegramView", EmployeeTelegramView.class)
        );
    }

    @Bean(name = "nullableStateUpdateProcessor")
    public UpdateProcessor<Update> nullableStateUpdateProcessor() {
        return new NullableStateEmployeeUP(
                employeeRepo,
                appContext.getBean("telegramView", EmployeeTelegramView.class)
        );
    }

    @Bean(name = "previousViewUpdateProcessor")
    public UpdateProcessor<Update> previousViewUpdateProcessor() {
        return new PreviousViewEmployeeUP(
                appUserRepo,
                appContext.getBean("telegramView", EmployeeTelegramView.class)
        );
    }
}

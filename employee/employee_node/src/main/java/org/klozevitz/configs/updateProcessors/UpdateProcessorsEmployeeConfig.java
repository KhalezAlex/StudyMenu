package org.klozevitz.configs.updateProcessors;

import lombok.RequiredArgsConstructor;
import org.klozevitz.EmployeeTelegramView;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.enitites.appUsers.enums.views.EmployeeView;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.repositories.appUsers.EmployeeRepo;
import org.klozevitz.services.implementations.main.MainService;
import org.klozevitz.services.implementations.updateProcessors.callbackQueryUpdateProcessors.byState.StudyStatePreviousViewUP;
import org.klozevitz.services.implementations.updateProcessors.commandUpdateProcessors.CommandEmployeeUP;
import org.klozevitz.services.implementations.updateProcessors.commandUpdateProcessors.byState.BasicStateEmployeeCUP;
import org.klozevitz.services.implementations.updateProcessors.util.NotRegisteredAppUserEmployeeUP;
import org.klozevitz.services.implementations.updateProcessors.util.NullableStateEmployeeUP;
import org.klozevitz.services.implementations.updateProcessors.util.PreviousViewEmployeeUP;
import org.klozevitz.services.implementations.updateProcessors.util.WrongAppUserRoleEmployeeUP;
import org.klozevitz.services.interfaces.main.AnswerProducer;
import org.klozevitz.services.interfaces.main.Main;
import org.klozevitz.services.interfaces.updateProcessors.UpdateProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.objects.Update;

@Configuration
@RequiredArgsConstructor
public class UpdateProcessorsEmployeeConfig {
    private final ApplicationContext appContext;
    private final AppUserRepo appUserRepo;
    private final EmployeeRepo employeeRepo;
    private final AnswerProducer answerProducer;

    @Bean
    public Main main() {
        return new MainService(
                appUserRepo,
                answerProducer,
                wrongAppUserRoleUpdateProcessor(),
                notRegisteredAppUserUpdateProcessor(),
                appContext.getBean("commandUpdateProcessor", UpdateProcessor.class),
                appContext.getBean("callbackQueryUpdateProcessor", UpdateProcessor.class)
                );
    }

    /**
     * UtilUpdateProcessors
     */
    @Bean
    public UpdateProcessor<Update, AppUser> wrongAppUserRoleUpdateProcessor() {
        return new WrongAppUserRoleEmployeeUP(
                appContext.getBean("telegramView", EmployeeTelegramView.class)
        );
    }

    @Bean
    public UpdateProcessor<Update, AppUser> notRegisteredAppUserUpdateProcessor() {
        return new NotRegisteredAppUserEmployeeUP(
                appContext.getBean("telegramView", EmployeeTelegramView.class)
        );
    }

    @Bean
    public UpdateProcessor<Update, Long> nullableStateUpdateProcessor() {
        return new NullableStateEmployeeUP(
                employeeRepo,
                appContext.getBean("telegramView", EmployeeTelegramView.class)
        );
    }

    @Bean(name = "previousViewUpdateProcessor")
    public UpdateProcessor<Update, EmployeeView> previousViewUpdateProcessor() {
        return new PreviousViewEmployeeUP(
                appContext.getBean("telegramView", EmployeeTelegramView.class)
        );
    }
}

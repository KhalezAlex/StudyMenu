package org.klozevitz.configs._legacy.updateProcessors;

import lombok.RequiredArgsConstructor;
import org.klozevitz.EmployeeTelegramView;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.enitites.appUsers.enums.views.EmployeeView;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.repositories.appUsers.EmployeeRepo;
import org.klozevitz.services.implementations.main.MainService;
import org.klozevitz.services.implementations.updateProcessors_LEGACY.util.NotRegisteredAppUserEmployeeUP_LEGACY;
import org.klozevitz.services.implementations.updateProcessors_LEGACY.util.NullableStateEmployeeUP_LEGACY;
import org.klozevitz.services.implementations.updateProcessors_LEGACY.util.PreviousViewEmployeeUP_LEGACY;
import org.klozevitz.services.implementations.updateProcessors_LEGACY.util.WrongAppUserRoleEmployeeUP_LEGACY;
import org.klozevitz.services.interfaces.main.AnswerProducer;
import org.klozevitz.services.interfaces.main.Main;
import org.klozevitz.services.interfaces.updateProcessors.UpdateProcessor_LEGACY;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.objects.Update;

@Configuration
@RequiredArgsConstructor
public class UpdateProcessorsEmployeeConfig_LEGACY {
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
                appContext.getBean("commandUpdateProcessor", UpdateProcessor_LEGACY.class),
                appContext.getBean("callbackQueryUpdateProcessor", UpdateProcessor_LEGACY.class)
        );
    }

    /**
     * UtilUpdateProcessors
     */
    @Bean
    public UpdateProcessor_LEGACY<Update, AppUser> wrongAppUserRoleUpdateProcessor() {
        return new WrongAppUserRoleEmployeeUP_LEGACY(
                appContext.getBean("telegramView", EmployeeTelegramView.class)
        );
    }

    @Bean
    public UpdateProcessor_LEGACY<Update, AppUser> notRegisteredAppUserUpdateProcessor() {
        return new NotRegisteredAppUserEmployeeUP_LEGACY(
                appContext.getBean("telegramView", EmployeeTelegramView.class)
        );
    }

    @Bean
    public UpdateProcessor_LEGACY<Update, Long> nullableStateUpdateProcessor() {
        return new NullableStateEmployeeUP_LEGACY(
                employeeRepo,
                appContext.getBean("telegramView", EmployeeTelegramView.class)
        );
    }

    @Bean(name = "previousViewUpdateProcessor")
    public UpdateProcessor_LEGACY<Update, EmployeeView> previousViewUpdateProcessor() {
        return new PreviousViewEmployeeUP_LEGACY(
                appUserRepo,
                appContext.getBean("telegramView", EmployeeTelegramView.class)
        );
    }
}

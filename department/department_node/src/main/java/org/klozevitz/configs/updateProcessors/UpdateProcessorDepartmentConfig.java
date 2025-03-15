package org.klozevitz.configs.updateProcessors;

import lombok.RequiredArgsConstructor;
import org.klozevitz.DepartmentTelegramView;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.services.implementations.main.MainService;
import org.klozevitz.services.implementations.updateProcessors.util.NotRegisteredAppUserDepartmentUP;
import org.klozevitz.services.implementations.updateProcessors.util.PreviousViewDepartmentUP;
import org.klozevitz.services.implementations.updateProcessors.util.WrongAppUserRoleDepartmentUP;
import org.klozevitz.services.interfaces.main.Main;
import org.klozevitz.services.interfaces.updateProcessors.UpdateProcessor;
import org.klozevitz.services.main.AnswerProducer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class UpdateProcessorDepartmentConfig {
    private final ApplicationContext appContext;
    private final AppUserRepo appUserRepo;
    private final AnswerProducer answerProducer;

    @Bean
    public Main main() {
        return new MainService(
                appUserRepo,
                answerProducer,
                appContext.getBean("wrongAppUserRoleUpdateProcessor", UpdateProcessor.class),
                appContext.getBean("notRegisteredAppUserUpdateProcessor", UpdateProcessor.class),
                appContext.getBean("commandUpdateProcessor", UpdateProcessor.class),
                appContext.getBean("textUpdateProcessor", UpdateProcessor.class),
                appContext.getBean("callbackQueryUpdateProcessor", UpdateProcessor.class)
//                appContext.getBean("documentUpdateProcessor", UpdateProcessor.class)
        );
    }

    @Bean(name = "wrongAppUserRoleUpdateProcessor")
    public UpdateProcessor wrongAppUserRoleUpdateProcessor() {
        return new WrongAppUserRoleDepartmentUP(
                appContext.getBean("telegramView", DepartmentTelegramView.class)
        );
    }

    @Bean(name = "notRegisteredAppUserUpdateProcessor")
    public UpdateProcessor notRegisteredAppUserUpdateProcessor() {
        return new NotRegisteredAppUserDepartmentUP(
                appContext.getBean("telegramView", DepartmentTelegramView.class)
        );
    }

    @Bean(name = "previousViewUpdateProcessor")
    public UpdateProcessor previousViewUpdateProcessor() {
        return new PreviousViewDepartmentUP(
                appUserRepo,
                appContext.getBean("telegramView", DepartmentTelegramView.class)
        );
    }
}

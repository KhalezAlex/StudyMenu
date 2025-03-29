package org.klozevitz.configs.updateProcessors;

import lombok.RequiredArgsConstructor;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.services.implementations.updateProcessors.command.CommandDepartmentUP;
import org.klozevitz.services.interfaces.updateProcessors.UpdateProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class CUP_DepartmentConfig {
    private final ApplicationContext appContext;
    private final AppUserRepo appUserRepo;

    @Bean(name = "commandUpdateProcessor")
    public UpdateProcessor commandUpdateProcessor() {
        return new CommandDepartmentUP(
                appUserRepo,
                appContext.getBean("welcomeViewResolver", UpdateProcessor.class),
                appContext.getBean("notRegisteredAppUserUpdateProcessor", UpdateProcessor.class),
                appContext.getBean("wrongAppUserRoleUpdateProcessor", UpdateProcessor.class),
                appContext.getBean("previousViewUpdateProcessor", UpdateProcessor.class)
        );
    }
}

package org.klozevitz.configs.updateProcessors;

import lombok.RequiredArgsConstructor;
import org.klozevitz.EmployeeTelegramView;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.services.implementations.updateProcessors.command.CommandEmployeeUP;
import org.klozevitz.services.interfaces.updateProcessors.UpdateProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class CUP_EmployeeConfig {
    private final ApplicationContext appContext;
    private final AppUserRepo appUserRepo;


    @Bean(name = "commandEmployeeUpdateProcessor")
    public UpdateProcessor commandEmployeeUpdateProcessor() {
        return new CommandEmployeeUP(
                appUserRepo,
                appContext.getBean("welcomeViewResolver", UpdateProcessor.class),
                appContext.getBean("notRegisteredAppUserUpdateProcessor", UpdateProcessor.class)
        );
    }
}

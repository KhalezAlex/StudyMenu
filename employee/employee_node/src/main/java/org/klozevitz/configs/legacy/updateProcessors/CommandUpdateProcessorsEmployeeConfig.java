package org.klozevitz.configs.legacy.updateProcessors;

import lombok.RequiredArgsConstructor;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.enitites.appUsers.enums.views.EmployeeView;
import org.klozevitz.services.implementations.updateProcessors_LEGACY.commandUpdateProcessors.CommandEmployeeUP_LEGACY;
import org.klozevitz.services.implementations.updateProcessors_LEGACY.commandUpdateProcessors.byState.BasicStateEmployeeCUP;
import org.klozevitz.services.interfaces.updateProcessors.UpdateProcessor_LEGACY;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class CommandUpdateProcessorsEmployeeConfig {
    private final ApplicationContext appContext;
    /**
     * CommandUpdateProcessors
     */
    @Bean(name = "commandUpdateProcessor")
    public UpdateProcessor_LEGACY<Update, AppUser> commandUpdateProcessor() {
        return new CommandEmployeeUP_LEGACY(
                appContext.getBean("nullableStateUpdateProcessor", UpdateProcessor_LEGACY.class),
                appContext.getBean("previousViewUpdateProcessor", UpdateProcessor_LEGACY.class),
                appContext.getBean("stateCommandDispatcher", Map.class)
        );
    }

    @Bean
    public Map<String, UpdateProcessor_LEGACY<Update, Long>> basicStateCommandDispatcher() {
        Map<String, UpdateProcessor_LEGACY<Update, Long>> dispatcher = new HashMap<>();

        dispatcher.put("/start", appContext.getBean("welcomeViewResolver", UpdateProcessor_LEGACY.class));

        return dispatcher;
    }

    @Bean(name = "basicStateCommandUpdateProcessor")
    public UpdateProcessor_LEGACY<Update, EmployeeView> basicStateCommandUpdateProcessor() {
        return new BasicStateEmployeeCUP(
                appContext.getBean("previousViewUpdateProcessor", UpdateProcessor_LEGACY.class),
                basicStateCommandDispatcher()
        );
    }
}

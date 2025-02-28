package org.klozevitz.configs.updateProcessors;

import lombok.RequiredArgsConstructor;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.enitites.appUsers.enums.views.EmployeeView;
import org.klozevitz.services.implementations.updateProcessors.commandUpdateProcessors.CommandEmployeeUP;
import org.klozevitz.services.implementations.updateProcessors.commandUpdateProcessors.byState.BasicStateEmployeeCUP;
import org.klozevitz.services.interfaces.updateProcessors.UpdateProcessor;
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
    public UpdateProcessor<Update, AppUser> commandUpdateProcessor() {
        return new CommandEmployeeUP(
                appContext.getBean("nullableStateUpdateProcessor", UpdateProcessor.class),
                appContext.getBean("previousViewUpdateProcessor", UpdateProcessor.class),
                appContext.getBean("stateCommandDispatcher", Map.class)
        );
    }

    @Bean
    public Map<String, UpdateProcessor<Update, Long>> basicStateCommandDispatcher() {
        Map<String, UpdateProcessor<Update, Long>> dispatcher = new HashMap<>();

        dispatcher.put("/start", appContext.getBean("welcomeViewResolver", UpdateProcessor.class));

        return dispatcher;
    }

    @Bean(name = "basicStateCommandUpdateProcessor")
    public UpdateProcessor<Update, EmployeeView> basicStateCommandUpdateProcessor() {
        return new BasicStateEmployeeCUP(
                appContext.getBean("previousViewUpdateProcessor", UpdateProcessor.class),
                basicStateCommandDispatcher()
        );
    }
}

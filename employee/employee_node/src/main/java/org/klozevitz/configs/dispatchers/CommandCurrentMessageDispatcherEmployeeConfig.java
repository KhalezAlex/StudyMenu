package org.klozevitz.configs.dispatchers;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.enitites.appUsers.enums.views.EmployeeView;
import org.klozevitz.services.interfaces.updateProcessors.UpdateProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

import static org.klozevitz.enitites.appUsers.enums.views.EmployeeView.NULL_STATE_ERROR_VIEW;

@Log4j
@RequiredArgsConstructor
public class CommandCurrentMessageDispatcherEmployeeConfig {
    private final ApplicationContext appContext;

    @Bean(name = "commandCurrentMessageDispatcher")
    public Map<EmployeeView, UpdateProcessor> commandCurrentMessageDispatcher() {
        final Map<EmployeeView, UpdateProcessor> dispatcher = new HashMap<>();

        dispatcher.put(
                NULL_STATE_ERROR_VIEW,
                appContext.getBean("nullableLastMessageUpdateProcessor", UpdateProcessor.class)
        );

        return dispatcher;
    }
}

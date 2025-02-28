package org.klozevitz.configs;

import lombok.RequiredArgsConstructor;
import org.klozevitz.enitites.appUsers.enums.states.EmployeeState;
import org.klozevitz.enitites.appUsers.enums.views.EmployeeView;
import org.klozevitz.services.interfaces.updateProcessors.UpdateProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Map;

import static org.klozevitz.enitites.appUsers.enums.states.EmployeeState.BASIC_STATE;

@Configuration
@RequiredArgsConstructor
public class StateDispatchersEmployeeConfig {
    private final ApplicationContext appContext;

    @Bean(name = "stateCommandDispatcher")
    public Map<EmployeeState, UpdateProcessor<Update, EmployeeView>> stateCommandDispatcher() {
        Map<EmployeeState, UpdateProcessor<Update, EmployeeView>> dispatcher = new HashMap<>();

        dispatcher.put(
                BASIC_STATE,
                appContext.getBean("basicStateCommandUpdateProcessor", UpdateProcessor.class)
        );
//        dispatcher.put(
//                STUDY_STATE,
//                appContext.getBean("studyStatePreviousViewUpdateProcessor", UpdateProcessor.class)
//        );

        return dispatcher;
    }

    @Bean(name = "stateCallbackQueryDispatcher")
    public Map<EmployeeState, UpdateProcessor<Update, EmployeeView>> stateCallbackQueryDispatcher() {
        Map<EmployeeState, UpdateProcessor<Update, EmployeeView>> dispatcher = new HashMap<>();

        dispatcher.put(
                BASIC_STATE,
                appContext.getBean("basicStateCallbackQueryUpdateProcessor", UpdateProcessor.class)
        );
//        dispatcher.put(
//                STUDY_STATE,
//                appContext.getBean("studyStateCallbackQueryUpdateProcessor", UpdateProcessor.class)
//        );

        return dispatcher;
    }
}

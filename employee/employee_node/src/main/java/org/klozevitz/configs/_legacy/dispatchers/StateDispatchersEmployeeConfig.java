package org.klozevitz.configs._legacy.dispatchers;

import lombok.RequiredArgsConstructor;
import org.klozevitz.enitites.appUsers.enums.states.EmployeeState;
import org.klozevitz.enitites.appUsers.enums.views.EmployeeView;
import org.klozevitz.services.interfaces.updateProcessors.UpdateProcessor_LEGACY;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Map;

import static org.klozevitz.enitites.appUsers.enums.states.EmployeeState.BASIC_STATE;

@RequiredArgsConstructor
public class StateDispatchersEmployeeConfig {
    private final ApplicationContext appContext;

    @Bean(name = "stateCommandDispatcher")
    public Map<EmployeeState, UpdateProcessor_LEGACY<Update, EmployeeView>> stateCommandDispatcher() {
        Map<EmployeeState, UpdateProcessor_LEGACY<Update, EmployeeView>> dispatcher = new HashMap<>();

        dispatcher.put(
                BASIC_STATE,
                appContext.getBean("basicStateCommandUpdateProcessor", UpdateProcessor_LEGACY.class)
        );
//        dispatcher.put(
//                STUDY_STATE,
//                appContext.getBean("studyStatePreviousViewUpdateProcessor", UpdateProcessor.class)
//        );

        return dispatcher;
    }

    @Bean(name = "stateCallbackQueryDispatcher")
    public Map<EmployeeState, UpdateProcessor_LEGACY<Update, EmployeeView>> stateCallbackQueryDispatcher() {
        Map<EmployeeState, UpdateProcessor_LEGACY<Update, EmployeeView>> dispatcher = new HashMap<>();

        dispatcher.put(
                BASIC_STATE,
                appContext.getBean("basicStateCallbackQueryUpdateProcessor", UpdateProcessor_LEGACY.class)
        );
//        dispatcher.put(
//                STUDY_STATE,
//                appContext.getBean("studyStateCallbackQueryUpdateProcessor", UpdateProcessor.class)
//        );

        return dispatcher;
    }
}

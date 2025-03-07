package org.klozevitz.configs.updateProcessors.dispatchers;

import lombok.RequiredArgsConstructor;
import org.klozevitz.enitites.appUsers.enums.views.EmployeeView;
import org.klozevitz.services.interfaces.updateProcessors.UpdateProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

import static org.klozevitz.enitites.appUsers.enums.views.EmployeeView.*;

@Configuration
@RequiredArgsConstructor
public class CallbackQueryCurrentMessageDispatcherEmployeeConfig {
    private final ApplicationContext appContext;

    @Bean(name = "callbackQueryCurrentMessageDispatcher")
    public Map<EmployeeView, UpdateProcessor> commandCurrentMessageDispatcher() {
        final Map<EmployeeView, UpdateProcessor> dispatcher = new HashMap<>();

        dispatcher.put(
                WELCOME_VIEW,
                appContext.getBean("welcomeViewCallbackQueryUpdateProcessor", UpdateProcessor.class)
        );
        dispatcher.put(
                CATEGORY_INFO_CHOICE_VIEW,
                appContext.getBean("categoryInfoChoiceViewCallbackQueryUpdateProcessor", UpdateProcessor.class)
        );
        dispatcher.put(
                CATEGORY_INFO_VIEW,
                appContext.getBean("categoryInfoViewCallbackQueryUpdateProcessor", UpdateProcessor.class)
        );
        dispatcher.put(
                CATEGORY_TEST_CHOICE_VIEW,
                appContext.getBean("categoryTestChoiceViewCallbackQueryUpdateProcessor", UpdateProcessor.class)
        );

        return dispatcher;
    }
}

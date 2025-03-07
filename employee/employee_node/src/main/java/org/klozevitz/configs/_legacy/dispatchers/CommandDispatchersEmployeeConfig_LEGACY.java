package org.klozevitz.configs._legacy.dispatchers;

import lombok.RequiredArgsConstructor;
import org.klozevitz.services.interfaces.updateProcessors.UpdateProcessor_LEGACY;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class CommandDispatchersEmployeeConfig_LEGACY {
    private final ApplicationContext appContext;

    @Bean(name = "basicStateCallbackQueryCommandDispatcher")
    public Map<String, UpdateProcessor_LEGACY<Update, Long>> basicStateCallbackQueryCommandDispatcher() {
        Map<String, UpdateProcessor_LEGACY<Update, Long>> dispatcher = new HashMap<>();

        dispatcher.put(
                "/start",
                appContext.getBean("welcomeViewResolver", UpdateProcessor_LEGACY.class)
        );
        dispatcher.put(
                "/category_info_choice_view",
                appContext.getBean("categoryChoiceViewResolver", UpdateProcessor_LEGACY.class)
        );
        dispatcher.put(
                "/category_test_choice_view",
                appContext.getBean("categoryTestChoiceViewResolver", UpdateProcessor_LEGACY.class));
        dispatcher.put(
                "/category_info_",
                appContext.getBean("categoryInfoViewResolver", UpdateProcessor_LEGACY.class)
        );

        return dispatcher;
    }
}

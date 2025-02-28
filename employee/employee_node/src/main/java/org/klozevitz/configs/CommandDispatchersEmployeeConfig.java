package org.klozevitz.configs;

import lombok.RequiredArgsConstructor;
import org.klozevitz.services.interfaces.updateProcessors.UpdateProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class CommandDispatchersEmployeeConfig {
    private final ApplicationContext appContext;

    @Bean(name = "basicStateCallbackQueryCommandDispatcher")
    public Map<String, UpdateProcessor<Update, Long>> basicStateCallbackQueryCommandDispatcher() {
        Map<String, UpdateProcessor<Update, Long>> dispatcher = new HashMap<>();

        dispatcher.put(
                "/start",
                appContext.getBean("welcomeViewResolver", UpdateProcessor.class)
        );
        dispatcher.put("/category_choice_view",
                appContext.getBean("categoryChoiceViewResolver", UpdateProcessor.class)
        );
        dispatcher.put(
                "/category_choice_",
                appContext.getBean("itemChoiceViewResolver", UpdateProcessor.class)
        );
        dispatcher.put(
                "/item_choice_",
                appContext.getBean("itemInfoViewResolver", UpdateProcessor.class)
        );

        return dispatcher;
    }

    @Bean(name = "studyStateCallbackQueryCommandDispatcher")
    public Map<String, UpdateProcessor<Update, Long>> studyStateCallbackQueryCommandDispatcher() {
        Map<String, UpdateProcessor<Update, Long>> dispatcher = new HashMap<>();

        dispatcher.put(
                "/start",
                appContext.getBean("welcomeViewResolver", UpdateProcessor.class)
        );

        return dispatcher;
    }

}

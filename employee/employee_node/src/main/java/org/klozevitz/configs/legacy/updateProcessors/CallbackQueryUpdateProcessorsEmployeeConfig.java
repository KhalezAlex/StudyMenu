package org.klozevitz.configs.legacy.updateProcessors;

import lombok.RequiredArgsConstructor;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.enitites.appUsers.enums.views.EmployeeView;
import org.klozevitz.services.implementations.updateProcessors_LEGACY.callbackQueryUpdateProcessors.CallbackQueryEmployeeUP;
import org.klozevitz.services.implementations.updateProcessors_LEGACY.callbackQueryUpdateProcessors.byState.BasicStateEmployeeCQUP;
import org.klozevitz.services.interfaces.updateProcessors.UpdateProcessor_LEGACY;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class CallbackQueryUpdateProcessorsEmployeeConfig {
    private final ApplicationContext appContext;
    /**
     * CallbackQueryUpdateProcessors
     * */

    @Bean
    public UpdateProcessor_LEGACY<Update, AppUser> callbackQueryUpdateProcessor() {
        return new CallbackQueryEmployeeUP(
                appContext.getBean("nullableStateUpdateProcessor", UpdateProcessor_LEGACY.class),
                appContext.getBean("previousViewUpdateProcessor", UpdateProcessor_LEGACY.class),
                appContext.getBean("stateCallbackQueryDispatcher", Map.class)
        );
    }

    @Bean(name = "basicStateCallbackQueryUpdateProcessor")
    public UpdateProcessor_LEGACY<Update, EmployeeView> basicStateCallbackQueryUpdateProcessor() {
        return new BasicStateEmployeeCQUP(
                appContext.getBean("previousViewUpdateProcessor", UpdateProcessor_LEGACY.class),
                appContext.getBean("basicStateCallbackQueryCommandDispatcher", Map.class)
        );
    }
}

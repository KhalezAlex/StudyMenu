package org.klozevitz.configs._legacy.updateProcessors;

import lombok.RequiredArgsConstructor;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.enitites.appUsers.enums.views.EmployeeView;
import org.klozevitz.services.implementations.updateProcessors_LEGACY.callbackQueryUpdateProcessors.CallbackQueryEmployeeUP_LEGACY;
import org.klozevitz.services.implementations.updateProcessors_LEGACY.callbackQueryUpdateProcessors.byState.BasicStateEmployeeCQUP;
import org.klozevitz.services.interfaces.updateProcessors.UpdateProcessor_LEGACY;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

@RequiredArgsConstructor
public class CallbackQueryUpdateProcessorsEmployeeConfig {
    private final ApplicationContext appContext;
    /**
     * CallbackQueryUpdateProcessors
     * */

    @Bean
    public UpdateProcessor_LEGACY<Update, AppUser> callbackQueryUpdateProcessor() {
        return new CallbackQueryEmployeeUP_LEGACY(
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

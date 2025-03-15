package org.klozevitz.configs.dispatchers;

import lombok.RequiredArgsConstructor;
import org.klozevitz.enitites.appUsers.enums.views.DepartmentView;
import org.klozevitz.services.interfaces.updateProcessors.UpdateProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

import static org.klozevitz.enitites.appUsers.enums.views.DepartmentView.*;

@Configuration
@RequiredArgsConstructor
public class CallbackQueryViewDispatcherDepartmentConfig {
    private final ApplicationContext appContext;

    @Bean(name = "callbackQueryViewDispatcher")
    public Map<DepartmentView, UpdateProcessor> callbackQueryViewDispatcher() {
        final Map<DepartmentView, UpdateProcessor> dispatcher = new HashMap<>();

        dispatcher.put(
                WELCOME_VIEW,
                appContext.getBean("welcomeViewCallbackQueryUpdateProcessor", UpdateProcessor.class)
        );

        dispatcher.put(
                RESOURCES_MANAGEMENT_VIEW,
                appContext.getBean("resourceManagementViewCallbackQueryUpdateProcessor", UpdateProcessor.class)
        );

        dispatcher.put(
                EMPLOYEES_MANAGEMENT_VIEW,
                appContext.getBean("employeeManagementViewCallbackQueryUpdateProcessor", UpdateProcessor.class)
        );

        dispatcher.put(
                EMPLOYEE_TG_ID_REQUEST_VIEW,
                appContext.getBean("employeeTgIdRequestViewCallbackQueryUpdateProcessor", UpdateProcessor.class)
        );

        dispatcher.put(
                EMPLOYEE_REGISTRATION_RESULT_VIEW,
                appContext.getBean("employeeRegistrationResultViewCallbackQueryUpdateProcessor", UpdateProcessor.class)
        );

        return dispatcher;
    }
}

package org.klozevitz.configs.dispatchers;

import lombok.RequiredArgsConstructor;
import org.klozevitz.enitites.appUsers.enums.views.DepartmentView;
import org.klozevitz.services.interfaces.updateProcessors.UpdateProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

import static org.klozevitz.enitites.appUsers.enums.views.DepartmentView.EMPLOYEE_TG_ID_REQUEST_VIEW;

@Configuration
@RequiredArgsConstructor
public class TextViewDispatcherDepartmentConfig {
    private final ApplicationContext appContext;

    @Bean(name = "textUpdateViewDispatcher")
    public Map<DepartmentView, UpdateProcessor> textViewDispatcher() {
        final Map<DepartmentView, UpdateProcessor> dispatcher = new HashMap<>();

        dispatcher.put(
                EMPLOYEE_TG_ID_REQUEST_VIEW,
                appContext.getBean("employeeTgIdRequestViewTextUpdateProcessor", UpdateProcessor.class)
        );

        return dispatcher;
    }
}

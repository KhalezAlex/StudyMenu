package org.klozevitz.configs.dispatchers;

import lombok.RequiredArgsConstructor;
import org.klozevitz.enitites.appUsers.enums.views.DepartmentView;
import org.klozevitz.services.interfaces.updateProcessors.UpdateProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

import static org.klozevitz.enitites.appUsers.enums.views.DepartmentView.RESOURCE_REQUEST_VIEW;

@Configuration
@RequiredArgsConstructor
public class DocumentViewDispatcherDepartmentConfig {
    private final ApplicationContext appContext;

    @Bean(name = "documentUpdateViewDispatcher")
    public Map<DepartmentView, UpdateProcessor> documentViewDispatcher() {
        final Map<DepartmentView, UpdateProcessor> dispatcher = new HashMap<>();

        dispatcher.put(
                RESOURCE_REQUEST_VIEW,
                appContext.getBean("resourceRequestViewDocumentUpdateProcessor", UpdateProcessor.class)
        );

        return dispatcher;
    }
}

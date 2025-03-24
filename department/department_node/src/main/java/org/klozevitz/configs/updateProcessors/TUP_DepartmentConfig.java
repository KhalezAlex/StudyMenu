package org.klozevitz.configs.updateProcessors;

import lombok.RequiredArgsConstructor;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.services.implementations.updateProcessors.text.TextDepartmentUP;
import org.klozevitz.services.implementations.updateProcessors.text.byView.EmployeeTgIdRequestViewTUP;
import org.klozevitz.services.interfaces.updateProcessors.Registrar;
import org.klozevitz.services.interfaces.updateProcessors.UpdateProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class TUP_DepartmentConfig {
    private final ApplicationContext appContext;
    private final AppUserRepo appUserRepo;
    private final Registrar employeeRegistrar;


    @Bean(name = "textUpdateProcessor")
    public UpdateProcessor textUpdateProcessor() {
        return new TextDepartmentUP(
                appUserRepo,
                appContext.getBean("textUpdateViewDispatcher", Map.class),
                appContext.getBean("notRegisteredAppUserUpdateProcessor", UpdateProcessor.class),
                appContext.getBean("wrongAppUserRoleUpdateProcessor", UpdateProcessor.class),
                appContext.getBean("previousViewUpdateProcessor", UpdateProcessor.class)
        );
    }

    @Bean(name = "employeeTgIdRequestViewTextUpdateProcessor")
    public UpdateProcessor employeeTgIdRequestViewTextUpdateProcessor() {
        return new EmployeeTgIdRequestViewTUP(
                employeeRegistrar
        );
    }
}

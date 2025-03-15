package org.klozevitz.configs.updateProcessors;

import lombok.RequiredArgsConstructor;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.repositories.appUsers.DepartmentRepo;
import org.klozevitz.services.implementations.updateProcessors.callback.CallbackQueryDepartmentUP;
import org.klozevitz.services.implementations.updateProcessors.callback.byView.*;
import org.klozevitz.services.interfaces.updateProcessors.UpdateProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class CQUP_DepartmentConfig {
    private final ApplicationContext appContext;
    private final AppUserRepo appUserRepo;
    private final DepartmentRepo departmentRepo;

    @Bean(name = "callbackQueryUpdateProcessor")
    public UpdateProcessor callbackQueryUpdateProcessor() {
        return new CallbackQueryDepartmentUP(
                appUserRepo,
                appContext.getBean("callbackQueryViewDispatcher", Map.class),
                appContext.getBean("notRegisteredAppUserUpdateProcessor", UpdateProcessor.class),
                appContext.getBean("previousViewUpdateProcessor", UpdateProcessor.class)
        );
    }

    @Bean(name = "welcomeViewCallbackQueryUpdateProcessor")
    public UpdateProcessor welcomeViewCallbackQueryUpdateProcessor() {
        return new WelcomeViewDepartmentCQUP(
                appUserRepo,
                appContext.getBean("previousViewUpdateProcessor", UpdateProcessor.class),
                appContext.getBean("employeeManagementViewResolver", UpdateProcessor.class),
                appContext.getBean("resourcesManagementViewResolver", UpdateProcessor.class),
                appContext.getBean("profileManagementViewResolver", UpdateProcessor.class),
                appContext.getBean("notRegisteredAppUserUpdateProcessor", UpdateProcessor.class)
        );
    }

    @Bean(name = "employeeManagementViewCallbackQueryUpdateProcessor")
    public UpdateProcessor employeeManagementViewCallbackQueryUpdateProcessor() {
        return new EmployeeManagementViewDepartmentCQUP(
                appUserRepo,
                appContext.getBean("previousViewUpdateProcessor", UpdateProcessor.class),
                appContext.getBean("welcomeViewResolver", UpdateProcessor.class),
                appContext.getBean("notRegisteredAppUserUpdateProcessor", UpdateProcessor.class),
                appContext.getBean("employeeTgIdRequestViewResolver", UpdateProcessor.class)
        );
    }

    @Bean(name = "employeeTgIdRequestViewCallbackQueryUpdateProcessor")
    public UpdateProcessor employeeTgIdRequestViewCallbackQueryUpdateProcessor() {
        return new EmployeeTgIdRequestViewCQUP(
                appContext.getBean("previousViewUpdateProcessor", UpdateProcessor.class),
                appContext.getBean("welcomeViewResolver", UpdateProcessor.class)
        );
    }

    @Bean(name = "resourceManagementViewCallbackQueryUpdateProcessor")
    public UpdateProcessor resourceManagementViewCallbackQueryUpdateProcessor() {
        return new ResourcesManagementViewDepartmentCQUP(
                appUserRepo,
                appContext.getBean("previousViewUpdateProcessor", UpdateProcessor.class),
                appContext.getBean("welcomeViewResolver", UpdateProcessor.class),
                appContext.getBean("notRegisteredAppUserUpdateProcessor", UpdateProcessor.class)
        );
    }

    @Bean(name = "employeeRegistrationResultViewCallbackQueryUpdateProcessor")
    public UpdateProcessor employeeRegistrationResultViewCallbackQueryUpdateProcessor() {
        return new EmployeeRegistrationResultViewCQUP(
                appContext.getBean("previousViewUpdateProcessor", UpdateProcessor.class),
                appContext.getBean("welcomeViewResolver", UpdateProcessor.class)
        );
    }

//    @Bean(name = "profileManagementViewCallbackQueryUpdateProcessor")
//    public UpdateProcessor profileManagementViewCallbackQueryUpdateProcessor() {
//        return new ProfileManagementViewDepartmentCQUP(
//                appUserRepo,
//                appContext.getBean("previousViewUpdateProcessor", UpdateProcessor.class),
//                appContext.getBean("welcomeViewUpdateProcessor", UpdateProcessor.class),
//                appContext.getBean("notRegisteredAppUserUpdateProcessor", UpdateProcessor.class)
//        );
//    }

//    @Bean(name = "employeeManagementViewResolver")
//    public UpdateProcessor employeeManagementViewResolver() {
//        return new EmployeeManagementViewResolver(
//                appUserRepo,
//                departmentRepo,
//                appContext.getBean("telegramView", DepartmentTelegramView.class),
//                appContext.getBean("viewResolver", UpdateProcessor.class)
//        );
//    }
}

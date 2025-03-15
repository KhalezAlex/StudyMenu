package org.klozevitz.configs.viewResolvers;

import lombok.RequiredArgsConstructor;
import org.klozevitz.DepartmentTelegramView;
import org.klozevitz.MessageUtil;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.repositories.appUsers.DepartmentRepo;
import org.klozevitz.services.implementations.updateProcessors.viewResolvers.*;
import org.klozevitz.services.interfaces.updateProcessors.UpdateProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ViewResolversDepartmentConfig {
    private final ApplicationContext appContext;
    private final AppUserRepo appUserRepo;
    private final DepartmentRepo departmentRepo;

    @Bean(name = "messageUtil")
    public MessageUtil messageUtil(){
        return new MessageUtil();
    }

    @Bean(name = "telegramView")
    public DepartmentTelegramView telegramView() {
        return new DepartmentTelegramView(
                messageUtil()
        );
    }

    @Bean(name = "welcomeViewResolver")
    public WelcomeViewResolver welcomeViewResolver() {
        return new WelcomeViewResolver(
                departmentRepo,
                telegramView()
        );
    }

    @Bean(name = "employeeManagementViewResolver")
    public UpdateProcessor employeeManagementViewResolver() {
        return new EmployeeManagementViewResolver(
                appUserRepo,
                departmentRepo,
                telegramView()
        );
    }

    @Bean(name = "employeeTgIdRequestViewResolver")
    public UpdateProcessor employeeTgIdRequestViewResolver() {
        return new EmployeeTgIdRequestViewResolver(
                departmentRepo,
                telegramView()
        );
    }

    @Bean(name = "resourcesManagementViewResolver")
    public UpdateProcessor resourcesManagementViewResolver() {
        return new ResourcesManagementViewResolver(
                appUserRepo,
                departmentRepo,
                telegramView(),
                appContext.getBean("notRegisteredAppUserUpdateProcessor", UpdateProcessor.class)
//                appContext.getBean("previousViewUpdateProcessor", UpdateProcessor.class)
        );
    }

    @Bean(name = "profileManagementViewResolver")
    public UpdateProcessor profileManagementViewResolver() {
        return new ProfileManagementViewResolver(
                appUserRepo,
                departmentRepo,
                telegramView(),
                appContext.getBean("previousViewUpdateProcessor", UpdateProcessor.class)
        );
    }
}

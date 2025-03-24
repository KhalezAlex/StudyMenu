package org.klozevitz.configs.updateProcessors;

import lombok.RequiredArgsConstructor;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.repositories.menu.CategoryRepo;
import org.klozevitz.repositories.menu.IngredientRepo;
import org.klozevitz.repositories.menu.ItemRepo;
import org.klozevitz.repositories.menu.TestQuestionRepo;
import org.klozevitz.services.CategoryService;
import org.klozevitz.services.implementations.updateProcessors.doc.DocumentDepartmentUP;
import org.klozevitz.services.implementations.updateProcessors.doc.byView.ResourceRequestViewDUP;
import org.klozevitz.services.implementations.updateProcessors.viewResolvers.ResourceUploadResultViewResolver;
import org.klozevitz.services.implementations.util.ExcelToTestParser;
import org.klozevitz.services.implementations.util.TestGenerator;
import org.klozevitz.services.interfaces.updateProcessors.UpdateProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class DUP_DepartmentConfig {
    private final ApplicationContext appContext;
    private final AppUserRepo appUserRepo;
    private final CategoryRepo categoryRepo;
    private final ItemRepo itemRepo;
    private final IngredientRepo ingredientRepo;
    private final TestQuestionRepo testQuestionRepo;


    @Bean(name = "documentUpdateProcessor")
    public UpdateProcessor documentUpdateProcessor() {
        return new DocumentDepartmentUP(
                appUserRepo,
                appContext.getBean("documentUpdateViewDispatcher", Map.class),
                appContext.getBean("notRegisteredAppUserUpdateProcessor", UpdateProcessor.class),
                appContext.getBean("previousViewUpdateProcessor", UpdateProcessor.class),
                appContext.getBean("wrongAppUserRoleUpdateProcessor", UpdateProcessor.class)
        );
    }

    @Bean(name = "resourceRequestViewDocumentUpdateProcessor")
    public UpdateProcessor resourceRequestViewDocumentUpdateProcessor() {
        return new ResourceRequestViewDUP(
                appUserRepo,
                appContext.getBean("previousViewUpdateProcessor", UpdateProcessor.class),
                appContext.getBean("resourceUploadResultViewResolver", ResourceUploadResultViewResolver.class),
                appContext.getBean("categoryService", CategoryService.class),
                appContext.getBean("excelParser", ExcelToTestParser.class),
                appContext.getBean("testGenerator", TestGenerator.class)
        );
    }
}

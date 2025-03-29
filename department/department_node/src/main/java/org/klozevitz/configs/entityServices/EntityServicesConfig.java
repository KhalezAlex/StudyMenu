package org.klozevitz.configs.entityServices;

import lombok.RequiredArgsConstructor;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.repositories.menu.CategoryRepo;
import org.klozevitz.services.CategoryService;
import org.klozevitz.services.CategoryServiceImplementation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class EntityServicesConfig {
    private final AppUserRepo appUserRepo;
    private final CategoryRepo categoryRepo;

    @Bean(name = "categoryService")
    public CategoryService categoryService() {
        return new CategoryServiceImplementation(
                categoryRepo
        );
    }
}

package org.klozevitz.services;

import lombok.RequiredArgsConstructor;
import org.klozevitz.enitites.menu.Category;
import org.klozevitz.repositories.menu.CategoryRepo;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class CategoryServiceImplementation implements CategoryService {
    private final CategoryRepo categoryRepo;

    @Override
    public void deleteByNameAndDepartmentIdCascade(String categoryName, long departmentId) {
        categoryRepo.deleteByNameAndDepartmentIdCascade(categoryName, departmentId);
    }

    @Override
    public void saveAll(Collection<Category> items) {
        categoryRepo.saveAll(items);
    }
}

package org.klozevitz.services;

import org.klozevitz.enitites.menu.Category;

import java.util.Collection;

public interface CategoryService {
    void deleteByNameAndDepartmentIdCascade(String categoryName, long departmentId);
    void saveAll(Collection<Category> items);
}

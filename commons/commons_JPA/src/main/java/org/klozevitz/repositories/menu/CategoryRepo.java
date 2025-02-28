package org.klozevitz.repositories.menu;

import org.klozevitz.enitites.menu.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {
    @Query(
            value = "SELECT * " +
                    "FROM category_t cat " +
                    "JOIN department_t det ON cat.department_id = det.id " +
                    "JOIN employee_t emt ON det.id = emt.department_id " +
                    "JOIN app_user_t aut ON emt.id = aut.employee_id " +
                    "WHERE aut.telegram_user_id = ?1 ;",
            nativeQuery = true
    )
    List<Category> categoriesByEmployeeTelegramUserId(long telegramUserId);
}

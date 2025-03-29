package org.klozevitz.repositories.appUsers;

import org.klozevitz.enitites.appUsers.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface DepartmentRepo extends JpaRepository<Department, Long> {
    @Query(
            value = "UPDATE department_t " +
                    "SET current_view = ?1 " +
                    "FROM app_user_t " +
                    "WHERE department_t.id = app_user_t.department_id " +
                    "AND app_user_t.telegram_user_id = ?2",
            nativeQuery = true
    )
    @Modifying
    @Transactional
    void setDepartmentCurrentView(String currentView, long telegramUserId);

    @Query(
            value = "UPDATE department_t " +
                    "SET state = ?1 " +
                    "FROM app_user_t " +
                    "WHERE department_t.id = app_user_t.department_id " +
                    "AND app_user_t.telegram_user_id = ?2",
            nativeQuery = true
    )
    @Modifying
    @Transactional
    void setDepartmentState(String state, long telegramUserId);
}

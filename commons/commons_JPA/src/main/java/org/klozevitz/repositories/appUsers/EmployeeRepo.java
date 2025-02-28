package org.klozevitz.repositories.appUsers;

import org.klozevitz.enitites.appUsers.Employee;
import org.klozevitz.enitites.appUsers.enums.views.EmployeeView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Long> {
    @Query(
            value = "UPDATE employee_t " +
                    "SET current_view = ?1 " +
                    "FROM app_user_t " +
                    "WHERE employee_t.id = app_user_t.employee_id " +
                    "AND app_user_t.telegram_user_id = ?2",
            nativeQuery = true
    )
    @Modifying
    @Transactional
    void setEmployeeCurrentView(String currentView, long telegramUserId);

    @Query(
            value = "UPDATE employee_t " +
                    "SET state = ?1 " +
                    "FROM app_user_t " +
                    "WHERE employee_t.id = app_user_t.employee_id " +
                    "AND app_user_t.telegram_user_id = ?2",
            nativeQuery = true
    )
    @Modifying
    @Transactional
    void setEmployeeState(String state, long telegramUserId);

    @Query(
            value = "SELECT emt.* " +
                    "FROM app_user_t aut " +
                    "JOIN employee_t emt ON aut.employee_id = emt.id " +
                    "WHERE aut.telegram_user_id = ?1 ;",
            nativeQuery = true
    )
    Employee findByAppUserTelegramUserId(long telegramUserId);

}

package org.klozevitz.repositories.appUsers;

import org.klozevitz.enitites.appUsers.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepo extends JpaRepository<AppUser, Long> {
    @Query(
            value = "SELECT * FROM app_user_t WHERE telegram_user_id = ?1",
            nativeQuery = true
    )
    Optional<AppUser> findByTelegramUserId(long telegramUserId);

    @Query(
            value = "SELECT * FROM app_user_t WHERE id = ?1",
            nativeQuery = true
    )
    Optional<AppUser> findById(long id);

    @Query(
            value = "SELECT * FROM app_user_t JOIN company_t ON app_user_t.company_id = company_t.id WHERE email = ?1",
            nativeQuery = true
    )
    Optional<AppUser> findByEmail(String email);
}

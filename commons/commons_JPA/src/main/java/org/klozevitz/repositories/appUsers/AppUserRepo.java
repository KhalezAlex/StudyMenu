package org.klozevitz.repositories.appUsers;

import org.klozevitz.enitites.appUsers.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepo extends JpaRepository<AppUser, Long> {
    AppUser findAppUserByTelegramUserId(long telegramUserId);
}

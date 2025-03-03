package org.klozevitz.repositories.appUsers;

import org.klozevitz.enitites.appUsers.MessageId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageIdRepo extends JpaRepository<MessageId, Long> {
}

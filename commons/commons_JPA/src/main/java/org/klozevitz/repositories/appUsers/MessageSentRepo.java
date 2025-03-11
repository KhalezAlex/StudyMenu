package org.klozevitz.repositories.appUsers;

import org.klozevitz.enitites.appUsers.MessageSent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface MessageSentRepo extends JpaRepository<MessageSent, Long> {
    @Modifying
    @Transactional
    @Query(
            value = "DELETE FROM message_sent_t " +
                    "WHERE id = ?1",
            nativeQuery = true
    )
    void deleteMessageById(long messageId);
}

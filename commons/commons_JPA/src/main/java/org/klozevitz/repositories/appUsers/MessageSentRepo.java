package org.klozevitz.repositories.appUsers;

import org.klozevitz.enitites.appUsers.MessageSent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface MessageSentRepo extends JpaRepository<MessageSent, Long> {
    @Query(
            value = "INSERT INTO message_sent_t (app_user_id, message_id)" +
                    "VALUES (?1, ?2)",
            nativeQuery = true
    )
    @Transactional
    @Modifying
    void save(long appUserId, int messageId);

    @Query(
            value = "DELETE FROM message_sent_t WHERE id = ?1",
            nativeQuery = true
    )
    @Transactional
    @Modifying
    void deleteMessageById(long messageId);
}

package org.klozevitz.repositories.appUsers;

import org.klozevitz.enitites.appUsers.MessageId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface MessageIdRepo extends JpaRepository<MessageId, Long> {
    @Query(
            value = "INSERT INTO message_id_t (app_user_id, message_id)" +
                    "VALUES (?1, ?2)",
            nativeQuery = true
    )
    @Transactional
    @Modifying
    void save(long appUserId, int messageId);

    @Query(
            value = "DELETE FROM message_id_t WHERE message_id = ?1",
            nativeQuery = true
    )
    @Transactional
    @Modifying
    void deleteMessageIdByMessageId(int messageId);
}

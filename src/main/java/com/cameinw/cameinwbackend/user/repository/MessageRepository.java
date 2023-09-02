package com.cameinw.cameinwbackend.user.repository;

import com.cameinw.cameinwbackend.user.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    @Query(value = "SELECT m1.* "
            + "FROM messages m1 "
            + "LEFT JOIN messages m2 ON ((m1.sender_id = m2.sender_id AND m1.receiver_id = m2.receiver_id) OR (m1.sender_id = m2.receiver_id AND m1.receiver_id = m2.sender_id)) "
            + "AND m1.timestamp < m2.timestamp "
            + "WHERE (m2.id IS NULL) AND (m1.sender_id = :userId OR m1.receiver_id = :userId) "
            + "ORDER BY m1.timestamp DESC", nativeQuery = true)
    List<Message> findMessagesPerUser(@Param("userId") Integer userId);

    @Query("SELECT m FROM Message m " +
            "WHERE (m.sender.id = :user1Id AND m.receiver.id = :user2Id) OR (m.sender.id = :user2Id AND m.receiver.id = :user1Id) " +
            "ORDER BY m.timestamp DESC")
    List<Message> findMessagesBetweenUsers(@Param("user1Id") Integer user1Id, @Param("user2Id") Integer user2Id);
}

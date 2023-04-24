package com.hangoutwithus.hangoutwithus.repository;

import com.hangoutwithus.hangoutwithus.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findAllByChatRoomId(Long chatRoomId);

    @Query(value = "SELECT m FROM Message m WHERE m.messageFrom.email <> :email AND m.chatRoom.id = :chatRoomId AND m.isRead = false")
    List<Message> findAllByChatRoomIdUnread(@Param("chatRoomId") Long chatRoomId, @Param("email") String email);
}

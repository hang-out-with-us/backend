package com.hangoutwithus.hangoutwithus.repository;

import com.hangoutwithus.hangoutwithus.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}

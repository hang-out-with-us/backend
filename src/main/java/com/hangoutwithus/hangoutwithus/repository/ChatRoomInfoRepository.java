package com.hangoutwithus.hangoutwithus.repository;

import com.hangoutwithus.hangoutwithus.entity.ChatRoomInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomInfoRepository extends JpaRepository<ChatRoomInfo, Long> {
}

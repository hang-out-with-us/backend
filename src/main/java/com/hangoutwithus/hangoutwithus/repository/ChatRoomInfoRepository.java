package com.hangoutwithus.hangoutwithus.repository;

import com.hangoutwithus.hangoutwithus.entity.ChatRoomInfo;
import com.hangoutwithus.hangoutwithus.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomInfoRepository extends JpaRepository<ChatRoomInfo, Long> {
    List<ChatRoomInfo> findAllByMember(Member member);

}

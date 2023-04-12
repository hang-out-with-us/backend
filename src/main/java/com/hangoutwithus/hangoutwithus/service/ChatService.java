package com.hangoutwithus.hangoutwithus.service;

import com.hangoutwithus.hangoutwithus.dto.ChatRoomResponse;
import com.hangoutwithus.hangoutwithus.entity.ChatRoom;
import com.hangoutwithus.hangoutwithus.entity.ChatRoomInfo;
import com.hangoutwithus.hangoutwithus.entity.Member;
import com.hangoutwithus.hangoutwithus.repository.ChatRoomInfoRepository;
import com.hangoutwithus.hangoutwithus.repository.ChatRoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomInfoRepository chatRoomInfoRepository;

    public ChatService(ChatRoomRepository chatRoomRepository, ChatRoomInfoRepository chatRoomInfoRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatRoomInfoRepository = chatRoomInfoRepository;
    }
    public List<ChatRoomResponse> getRooms() {
        return chatRoomRepository.findAll().stream().map(this::entityToDto).collect(Collectors.toList());

    }

    public void createRoom(Member me, Member target) {
        ChatRoom chatRoom = new ChatRoom();
        ChatRoomInfo chatRoomInfo1 = ChatRoomInfo.builder()
                .chatRoom(chatRoom)
                .member(me)
                .build();
        ChatRoomInfo chatRoomInfo2 = ChatRoomInfo.builder()
                .chatRoom(chatRoom)
                .member(target)
                .build();

        chatRoomRepository.save(chatRoom);
        chatRoomInfoRepository.save(chatRoomInfo1);
        chatRoomInfoRepository.save(chatRoomInfo2);
    }
        //entity를 dto로 변환
    private ChatRoomResponse entityToDto(ChatRoom chatRoom) {
        return new ChatRoomResponse(chatRoom.getId());
    }
}

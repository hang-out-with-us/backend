package com.hangoutwithus.hangoutwithus.service;

import com.hangoutwithus.hangoutwithus.dto.ChatRoomResponse;
import com.hangoutwithus.hangoutwithus.dto.MessageDto;
import com.hangoutwithus.hangoutwithus.entity.ChatRoom;
import com.hangoutwithus.hangoutwithus.entity.ChatRoomInfo;
import com.hangoutwithus.hangoutwithus.entity.Member;
import com.hangoutwithus.hangoutwithus.repository.ChatRoomInfoRepository;
import com.hangoutwithus.hangoutwithus.repository.ChatRoomRepository;
import com.hangoutwithus.hangoutwithus.repository.MemberRepository;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomInfoRepository chatRoomInfoRepository;
    private final MemberRepository memberRepository;

    private final SimpMessageSendingOperations messagingTemplate;

    public ChatService(ChatRoomRepository chatRoomRepository, ChatRoomInfoRepository chatRoomInfoRepository, MemberRepository memberRepository, SimpMessageSendingOperations messagingTemplate) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatRoomInfoRepository = chatRoomInfoRepository;
        this.memberRepository = memberRepository;
        this.messagingTemplate = messagingTemplate;
    }
    public List<ChatRoomResponse> getRooms(Principal principal) {
        return chatRoomInfoRepository.findAllByMember(memberRepository.findMemberByEmail(principal.getName()).orElseThrow())
                .stream()
                .map(chatRoomInfo -> entityToDto(chatRoomInfo.getChatRoom())).collect(Collectors.toList());
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

    public void send(MessageDto messageDto, Principal principal) {
messagingTemplate.convertAndSend("/topic/chat/" + messageDto.getChatRoomId(), messageDto);
    }
}

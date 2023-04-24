package com.hangoutwithus.hangoutwithus.service;

import com.hangoutwithus.hangoutwithus.dto.ChatRoomResponse;
import com.hangoutwithus.hangoutwithus.dto.MessageDto;
import com.hangoutwithus.hangoutwithus.entity.ChatRoom;
import com.hangoutwithus.hangoutwithus.entity.ChatRoomInfo;
import com.hangoutwithus.hangoutwithus.entity.Member;
import com.hangoutwithus.hangoutwithus.entity.Message;
import com.hangoutwithus.hangoutwithus.repository.ChatRoomInfoRepository;
import com.hangoutwithus.hangoutwithus.repository.ChatRoomRepository;
import com.hangoutwithus.hangoutwithus.repository.MemberRepository;
import com.hangoutwithus.hangoutwithus.repository.MessageRepository;
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
    private final MessageRepository messageRepository;
    private final SimpMessageSendingOperations messagingTemplate;

    public ChatService(ChatRoomRepository chatRoomRepository, ChatRoomInfoRepository chatRoomInfoRepository, MemberRepository memberRepository, MessageRepository messageRepository, SimpMessageSendingOperations messagingTemplate) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatRoomInfoRepository = chatRoomInfoRepository;
        this.memberRepository = memberRepository;
        this.messageRepository = messageRepository;
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
        Message message = messageRepository.save(Message.builder().
                chatRoom(chatRoomRepository.findById(messageDto.getChatRoomId()).orElseThrow())
                .messageFrom(memberRepository.findMemberByEmail(principal.getName()).orElseThrow())
                .content(messageDto.getContent())
                .build());

        messageDto.setCreatedDate(message.getCreatedDate());
        messageDto.setId(message.getId());
        messagingTemplate.convertAndSend("/topic/chat/" + messageDto.getChatRoomId(), messageDto);
    }

    public void read(MessageDto messageDto) {
        Message message = messageRepository.findById(messageDto.getId()).orElseThrow();
        message.readMessage();
        messageRepository.save(message);
    }

    public List<MessageDto> getMessages(String roomId, Principal principal) {
        List<Message> messages = messageRepository.findAllByChatRoomIdUnread(Long.parseLong(roomId), principal.getName());
        messages.forEach(message -> {
                    message.readMessage();
                    messageRepository.save(message);
                });
        return messages.stream().map(MessageDto::new).collect(Collectors.toList());
    }
}

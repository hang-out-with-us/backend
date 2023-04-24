package com.hangoutwithus.hangoutwithus.controller;

import com.hangoutwithus.hangoutwithus.dto.ChatRoomResponse;
import com.hangoutwithus.hangoutwithus.dto.MessageDto;
import com.hangoutwithus.hangoutwithus.service.ChatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/chat/rooms")
    public List<ChatRoomResponse> getRooms(Principal principal) {
        return chatService.getRooms(principal);
    }

    @GetMapping("/chat/{roomId}")
    public List<MessageDto> getMessages(@PathVariable String roomId,Principal principal) {
        return chatService.getMessages(roomId,principal);
    }

    @MessageMapping("/message")
    public void message(@Payload MessageDto messageDto,Principal principal){
        chatService.send(messageDto, principal);
    }

    @MessageMapping("/read")
    public void read(@Payload MessageDto messageDto) {
        chatService.read(messageDto);
    }
}
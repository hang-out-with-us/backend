package com.hangoutwithus.hangoutwithus.controller;

import com.hangoutwithus.hangoutwithus.dto.ChatRoomResponse;
import com.hangoutwithus.hangoutwithus.dto.MessageDto;
import com.hangoutwithus.hangoutwithus.service.ChatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/rooms")
    public List<ChatRoomResponse> getRooms(Principal principal) {
        return chatService.getRooms(principal);
    }

    @MessageMapping("/message")
    public void message(@Payload MessageDto messageDto,Principal principal){
        chatService.send(messageDto, principal);
    }
}

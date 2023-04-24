package com.hangoutwithus.hangoutwithus.dto;

import com.hangoutwithus.hangoutwithus.entity.Message;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class MessageDto {
    Long id;
    String content;
    String sender;
    Long chatRoomId;
    LocalDateTime createdDate;

    public MessageDto(Message message) {
        this.id = message.getId();
        this.content = message.getContent();
        this.sender = message.getMessageFrom().getName();
        this.chatRoomId = message.getChatRoom().getId();
        this.createdDate = message.getCreatedDate();
    }
}

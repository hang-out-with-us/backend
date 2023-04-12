package com.hangoutwithus.hangoutwithus.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MessageDto {
    String content;
    Long chatRoomId;
}

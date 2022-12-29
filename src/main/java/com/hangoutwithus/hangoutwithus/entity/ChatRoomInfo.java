package com.hangoutwithus.hangoutwithus.entity;


import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class ChatRoomInfo{
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn
    private Member member;

    @ManyToOne
    @JoinColumn
    private ChatRoom chatRoom;
}

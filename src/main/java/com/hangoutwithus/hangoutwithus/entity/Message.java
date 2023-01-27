package com.hangoutwithus.hangoutwithus.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Message extends BaseEntityCreatedOnly {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne
    private ChatRoom chatRoom;

    @OneToOne
    private Message messageFrom;
}

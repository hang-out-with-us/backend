package com.hangoutwithus.hangoutwithus.entity;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
public class ChatRoom extends BaseEntity{

    @Id
    @GeneratedValue
    private Long id;

}

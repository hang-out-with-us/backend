package com.hangoutwithus.hangoutwithus.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Member extends BaseEntity{
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String email;

    private String password;

    private Integer age;

    @OneToOne
    @JoinColumn
    private Post post;

}

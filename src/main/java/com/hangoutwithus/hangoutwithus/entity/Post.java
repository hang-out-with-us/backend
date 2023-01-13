package com.hangoutwithus.hangoutwithus.entity;


import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@Getter
public class Post extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    private String image;

    private String content;

    private Integer locationX;

    private Integer locationY;

    private String areaName;

    @OneToOne(mappedBy = "post")
    private Member member;
}

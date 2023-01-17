package com.hangoutwithus.hangoutwithus.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    public void updatePost(String image, String content, Integer locationX, Integer locationY, String areaName) {
        this.image = image;
        this.content = content;
        this.locationX = locationX;
        this.locationY = locationY;
        this.areaName = areaName;
    }
}

package com.hangoutwithus.hangoutwithus.dto;

import com.hangoutwithus.hangoutwithus.entity.Post;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostDto {
    String image;
    String content;

    Integer locationX;

    Integer locationY;

    String areaName;

    public PostDto(Post post) {
        this.image = post.getImage();
        this.content = post.getContent();
        this.locationX = post.getLocationX();
        this.locationY = post.getLocationY();
        this.areaName = post.getAreaName();
    }
}

package com.hangoutwithus.hangoutwithus.dto;

import com.hangoutwithus.hangoutwithus.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostResponse {
    Long id;

    String image;

    String content;

    Integer locationX;

    Integer locationY;

    String areaName;

    public PostResponse(Post post) {
        this.id = post.getId();
        this.image = post.getImage();
        this.content = post.getContent();
        this.locationX = post.getLocationX();
        this.locationY = post.getLocationY();
        this.areaName = post.getAreaName();
    }

}

package com.hangoutwithus.hangoutwithus.dto;

import com.hangoutwithus.hangoutwithus.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostRequest {
    List<MultipartFile> images = new ArrayList<>();

    String content;

    Integer locationX;

    Integer locationY;

    String areaName;

    public PostRequest(Post post) {
        this.content = post.getContent();
        this.locationX = post.getLocationX();
        this.locationY = post.getLocationY();
        this.areaName = post.getAreaName();
    }
}

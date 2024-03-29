package com.hangoutwithus.hangoutwithus.dto;

import com.hangoutwithus.hangoutwithus.entity.Image;
import com.hangoutwithus.hangoutwithus.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class PostResponse {
    Long id;

    String content;

    List<String> filenames = new ArrayList<>();

    public PostResponse(Post post) {
        this.id = post.getId();
        this.content = post.getContent();
        if(post.getImages() != null) {
            this.filenames.addAll(post.getImages().stream().map(Image::getName).collect(Collectors.toList()));
        }
    }

    public void addFilename(String filename) {
        filenames.add(filename);
    }

}

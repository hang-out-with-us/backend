package com.hangoutwithus.hangoutwithus.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private Integer locationX;

    private Integer locationY;

    private String areaName;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    public void updatePost(String content, Integer locationX, Integer locationY, String areaName) {
        this.content = content;
        this.locationX = locationX;
        this.locationY = locationY;
        this.areaName = areaName;
    }

    public void addImage(Image image) {
        images.add(image);
    }

    public void addAllImages(List<Image> images) {
        this.images.addAll(images);
    }
}

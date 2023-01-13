package com.hangoutwithus.hangoutwithus.dto;

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
}

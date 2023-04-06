package com.hangoutwithus.hangoutwithus.dto;

import com.hangoutwithus.hangoutwithus.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberRecommendResponse {

    Long id;

    String name;

    String email;

    Integer age;

    PostResponse post;


    public MemberRecommendResponse(Member member, PostResponse postResponse) {
        this.id = member.getId();
        this.name = member.getName();
        this.email = member.getEmail();
        this.age = member.getAge();
        this.post = postResponse;
    }
}

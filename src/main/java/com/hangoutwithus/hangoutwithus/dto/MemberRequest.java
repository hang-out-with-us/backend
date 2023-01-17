package com.hangoutwithus.hangoutwithus.dto;


import com.hangoutwithus.hangoutwithus.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class MemberRequest {
    String name;

    String email;

    String password;

    Integer age;

    public MemberRequest(Member member) {
        this.name = member.getName();
        this.email = member.getEmail();
        this.age = member.getAge();
    }
}

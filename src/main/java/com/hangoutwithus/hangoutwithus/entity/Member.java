package com.hangoutwithus.hangoutwithus.entity;

import com.hangoutwithus.hangoutwithus.dto.MemberBaseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Member extends BaseEntity {
    @Id
    @GeneratedValue
    @Column
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Integer age;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    public Member(MemberBaseDto memberBaseDto) {
        this.name = memberBaseDto.getName();
        this.email = memberBaseDto.getEmail();
        this.password = memberBaseDto.getPassword();
        this.age = memberBaseDto.getAge();
    }

    public void addPost(Post post) {
        this.post = post;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

}

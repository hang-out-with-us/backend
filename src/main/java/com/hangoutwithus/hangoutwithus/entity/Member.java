package com.hangoutwithus.hangoutwithus.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String name;

    @Column(unique = true)
    private String email;

    @Column
    private String password;

    @Column
    private Integer age;

    @Column
    private Boolean isCompletedSignup;

    @OneToOne(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Post post;

    @OneToOne(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Geolocation geolocation;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ChatRoomInfo> chatRooms;

    @OneToMany(mappedBy = "likeTo", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<MemberLike> likeToList;

    @OneToMany(mappedBy = "likeFrom", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<MemberLike> likeFromList;


    @Enumerated(EnumType.STRING)
    private Role role;


    public void addPost(Post post) {
        this.post = post;
    }

    public void update(String name, String email, String password, Integer age) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.age = age;
        this.isCompletedSignup = true;
    }
}

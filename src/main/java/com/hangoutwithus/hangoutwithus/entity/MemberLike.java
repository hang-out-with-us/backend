package com.hangoutwithus.hangoutwithus.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class MemberLike {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "like_from")
    private Member likeFrom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "like_to")
    private Member likeTo;
}

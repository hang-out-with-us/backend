package com.hangoutwithus.hangoutwithus.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Image extends BaseEntity {
    @Id
    String name;

    @ManyToOne
    Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    Post post;
}
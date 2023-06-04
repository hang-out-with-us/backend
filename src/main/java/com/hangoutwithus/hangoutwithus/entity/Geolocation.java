package com.hangoutwithus.hangoutwithus.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Geolocation {
    @Id
    @GeneratedValue
    private Long id;

    private Double latitude;

    private Double longitude;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void update(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}

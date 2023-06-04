package com.hangoutwithus.hangoutwithus.repository;

import com.hangoutwithus.hangoutwithus.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findMemberByEmail(String email);

    void deleteMemberByEmail(String email);

    @Query("select m from Member m order by (power (:latitude-m.geolocation.latitude,2) + power(:longitude-m.geolocation.longitude,2))")
    Slice<Member> findByDistance(@Param("latitude") Double latitude, @Param("longitude") Double longitude, Pageable pageable);
}

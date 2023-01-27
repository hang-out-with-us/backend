package com.hangoutwithus.hangoutwithus.repository;

import com.hangoutwithus.hangoutwithus.entity.Member;
import com.hangoutwithus.hangoutwithus.entity.MemberLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberLikeRepository extends JpaRepository<MemberLike, Long> {

    List<MemberLike> findMemberLikesByLikeTo(Member likeTo);
}

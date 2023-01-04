package com.hangoutwithus.hangoutwithus.service;

import com.hangoutwithus.hangoutwithus.dto.MemberBaseDto;
import com.hangoutwithus.hangoutwithus.entity.Member;
import com.hangoutwithus.hangoutwithus.repository.MemberRepository;
import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Getter
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberBaseDto create(MemberBaseDto memberBaseDto) {
        Member member = new Member(memberBaseDto);
        return new MemberBaseDto(memberRepository.save(member));
    }

    @Transactional(readOnly = true)
    public MemberBaseDto findOne(Long id) {
        Member member = memberRepository.findById(id).orElseThrow();
        return new MemberBaseDto(member);
    }

    public void delete(Long id) {
        memberRepository.deleteById(id);
    }

    public MemberBaseDto updatePassword(Long id, String password) {
        Member member = memberRepository.findById(id).orElseThrow();
        return new MemberBaseDto(member);
    }

    public MemberBaseDto updateName(Long id, String name) {
        Member member = memberRepository.findById(id).orElseThrow();
        return new MemberBaseDto(member);
    }

    public MemberBaseDto updateEmail(Long id, String email) {
        Member member = memberRepository.findById(id).orElseThrow();
        member.updateEmail(email);
        return new MemberBaseDto(member);
    }
}

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
        Member member = Member.builder()
                .name(memberBaseDto.getName())
                .email(memberBaseDto.getEmail())
                .password(memberBaseDto.getPassword())
                .age(memberBaseDto.getAge())
                .build();
        return new MemberBaseDto(memberRepository.save(member));
    }

    @Transactional(readOnly = true)
    public MemberBaseDto findOne(Long id) {
        Member member = memberRepository.findById(id).orElseThrow();
        return new MemberBaseDto(member);
    }

    public MemberBaseDto update(Long id, MemberBaseDto memberBaseDto) {
        Member member = memberRepository.findById(id).orElseThrow();
        String name = memberBaseDto.getName() == null ? member.getName() : memberBaseDto.getName();
        String email = memberBaseDto.getEmail() == null ? member.getEmail() : memberBaseDto.getEmail();
        String password = memberBaseDto.getPassword() == null ? member.getPassword() : memberBaseDto.getPassword();
        Integer age = memberBaseDto.getAge() == null ? member.getAge() : memberBaseDto.getAge();
        member.update(name, email, password, age);

        return new MemberBaseDto(member);
    }

    public void delete(Long id) {
        memberRepository.deleteById(id);
    }
}

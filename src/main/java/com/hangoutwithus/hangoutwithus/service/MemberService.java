package com.hangoutwithus.hangoutwithus.service;

import com.hangoutwithus.hangoutwithus.dto.MemberRequest;
import com.hangoutwithus.hangoutwithus.dto.MemberResponse;
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

    public MemberResponse create(MemberRequest memberRequest) {
        Member member = Member.builder()
                .name(memberRequest.getName())
                .email(memberRequest.getEmail())
                .password(memberRequest.getPassword())
                .age(memberRequest.getAge())
                .build();
        return new MemberResponse(memberRepository.save(member));
    }

    @Transactional(readOnly = true)
    public MemberResponse findOne(Long id) {
        Member member = memberRepository.findById(id).orElseThrow();
        return new MemberResponse(member);
    }

    public MemberResponse update(Long id, MemberRequest memberRequest) {
        Member member = memberRepository.findById(id).orElseThrow();
        String name = memberRequest.getName() == null ? member.getName() : memberRequest.getName();
        String email = memberRequest.getEmail() == null ? member.getEmail() : memberRequest.getEmail();
        String password = memberRequest.getPassword() == null ? member.getPassword() : memberRequest.getPassword();
        Integer age = memberRequest.getAge() == null ? member.getAge() : memberRequest.getAge();
        member.update(name, email, password, age);

        return new MemberResponse(member);
    }

    public void delete(Long id) {
        memberRepository.deleteById(id);
    }
}

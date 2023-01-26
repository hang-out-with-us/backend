package com.hangoutwithus.hangoutwithus.service;

import com.hangoutwithus.hangoutwithus.dto.MemberRequest;
import com.hangoutwithus.hangoutwithus.dto.MemberResponse;
import com.hangoutwithus.hangoutwithus.entity.Member;
import com.hangoutwithus.hangoutwithus.jwt.JwtTokenProvider;
import com.hangoutwithus.hangoutwithus.repository.MemberRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Slf4j
@Service
@Getter
@Transactional
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder, AuthenticationManagerBuilder authenticationManagerBuilder, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.jwtTokenProvider = jwtTokenProvider;
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

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return memberRepository.findMemberByEmail(email)
                .map(member -> createUser(email, member))
                .orElseThrow(() -> new UsernameNotFoundException(email + "데이터베이스에서 찾을 수 없습니다."));
//        return null;
    }

    private User createUser(String email, Member member) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getRole().value());
        return new User(member.getEmail(), member.getPassword(), Collections.singleton(grantedAuthority));
    }
}

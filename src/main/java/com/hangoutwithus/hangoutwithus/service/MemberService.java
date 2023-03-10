package com.hangoutwithus.hangoutwithus.service;

import com.hangoutwithus.hangoutwithus.dto.LoginDto;
import com.hangoutwithus.hangoutwithus.dto.MemberRequest;
import com.hangoutwithus.hangoutwithus.dto.MemberResponse;
import com.hangoutwithus.hangoutwithus.dto.TokenDto;
import com.hangoutwithus.hangoutwithus.entity.Member;
import com.hangoutwithus.hangoutwithus.entity.MemberLike;
import com.hangoutwithus.hangoutwithus.jwt.JwtFilter;
import com.hangoutwithus.hangoutwithus.jwt.JwtTokenProvider;
import com.hangoutwithus.hangoutwithus.repository.MemberLikeRepository;
import com.hangoutwithus.hangoutwithus.repository.MemberRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Getter
@Transactional
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final MemberLikeRepository memberLikeRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;


    //CRUD
    public MemberService(MemberRepository memberRepository, MemberLikeRepository memberLikeRepository, PasswordEncoder passwordEncoder, AuthenticationManagerBuilder authenticationManagerBuilder, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.memberLikeRepository = memberLikeRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public MemberResponse signup(MemberRequest memberRequest) {

        Member member = Member.builder()
                .name(memberRequest.getName())
                .email(memberRequest.getEmail())
                .password(passwordEncoder.encode(memberRequest.getPassword()))
                .age(memberRequest.getAge())
                .role(memberRequest.getRole())
                .build();

        return new MemberResponse(memberRepository.save(member));
    }

    public ResponseEntity<TokenDto> authorize(LoginDto loginDto) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtTokenProvider.createToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public MemberResponse findOne(Long id) {
        Member member = memberRepository.findById(id).orElseThrow();
        return new MemberResponse(member);
    }

    public MemberResponse update(Principal principal, MemberRequest memberRequest) {
        Member member = memberRepository.findMemberByEmail(principal.getName()).orElseThrow();
        String name = memberRequest.getName() == null ? member.getName() : memberRequest.getName();
        String email = memberRequest.getEmail() == null ? member.getEmail() : memberRequest.getEmail();
        String password = memberRequest.getPassword() == null ? member.getPassword() : passwordEncoder.encode(memberRequest.getPassword());
        Integer age = memberRequest.getAge() == null ? member.getAge() : memberRequest.getAge();
        member.update(name, email, password, age);

        return new MemberResponse(member);
    }

    public void delete(Principal principal) {
        memberRepository.deleteMemberByEmail(principal.getName());
    }

    //Like
    public void like(Principal principal, Long id) {
        Member me = memberRepository.findMemberByEmail(principal.getName()).orElseThrow();
        Member target = memberRepository.findById(id).orElseThrow();
        MemberLike memberLike = MemberLike.builder()
                .likeTo(target)
                .likeFrom(me)
                .build();
        memberLikeRepository.save(memberLike);
    }

    public List<MemberResponse> listWhoLikeMe(Principal principal) {
        Member member = memberRepository.findMemberByEmail(principal.getName()).orElseThrow();
        List<MemberLike> memberLikes = memberLikeRepository.findMemberLikesByLikeTo(member);
        List<Member> membersWhoLikeMe = memberLikes.stream().map(MemberLike::getLikeFrom).collect(Collectors.toList());
        return membersWhoLikeMe.stream().map(MemberResponse::new).collect(Collectors.toList());
    }

    public Slice<MemberResponse> recommend(Principal principal, Pageable pageable) {
        Member member = memberRepository.findMemberByEmail(principal.getName()).orElseThrow();
        Slice<Member> memberPage = memberRepository.findByDistance(member.getPost().getLocationX(), member.getPost().getLocationY(), pageable);
        return memberPage.map(MemberResponse::new);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return memberRepository.findMemberByEmail(email)
                .map(member -> createUser(email, member))
                .orElseThrow(() -> new UsernameNotFoundException(email + "???????????????????????? ?????? ??? ????????????."));
//        return null;
    }

    private User createUser(String email, Member member) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getRole().value());
        return new User(member.getEmail(), member.getPassword(), Collections.singleton(grantedAuthority));
    }
}

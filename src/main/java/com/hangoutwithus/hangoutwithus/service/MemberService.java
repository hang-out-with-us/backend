package com.hangoutwithus.hangoutwithus.service;

import com.hangoutwithus.hangoutwithus.dto.*;
import com.hangoutwithus.hangoutwithus.entity.Geolocation;
import com.hangoutwithus.hangoutwithus.entity.Member;
import com.hangoutwithus.hangoutwithus.entity.MemberLike;
import com.hangoutwithus.hangoutwithus.entity.Post;
import com.hangoutwithus.hangoutwithus.jwt.JwtTokenProvider;
import com.hangoutwithus.hangoutwithus.repository.GeolocationRepository;
import com.hangoutwithus.hangoutwithus.repository.MemberLikeRepository;
import com.hangoutwithus.hangoutwithus.repository.MemberRepository;
import com.hangoutwithus.hangoutwithus.repository.RefreshTokenRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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
import org.springframework.web.server.ResponseStatusException;

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
    private final ChatService chatService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final GeolocationRepository geolocationRepository;

    @Value("${file.path}")
    String path;


    //CRUD
    public MemberService(MemberRepository memberRepository, MemberLikeRepository memberLikeRepository, PasswordEncoder passwordEncoder, AuthenticationManagerBuilder authenticationManagerBuilder, JwtTokenProvider jwtTokenProvider, ChatService chatService, RefreshTokenRepository refreshTokenRepository, GeolocationRepository geolocationRepository) {
        this.memberRepository = memberRepository;
        this.memberLikeRepository = memberLikeRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.chatService = chatService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.geolocationRepository = geolocationRepository;
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

        String refreshJwt = jwtTokenProvider.createRefreshToken(authentication);

        return new ResponseEntity<>(new TokenDto(jwt, refreshJwt), HttpStatus.OK);
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

        //나 자신 좋아요 안 됨
        if (me.getId() == id) {
            return;
        }
        Member target = memberRepository.findById(id).orElseThrow();
        if (me.equals(target)) {
            throw new IllegalArgumentException("자기 자신을 좋아요 할 수 없습니다.");
        }
        MemberLike memberLike = MemberLike.builder()
                .likeTo(target)
                .likeFrom(me)
                .build();
        memberLikeRepository.save(memberLike);

        //상대방도 나를 좋아요 눌렀으면 자동으로 채팅방 생성
        if (memberLikeRepository.findMemberLikesByLikeTo(me).stream().anyMatch(memberLike1 -> memberLike1.getLikeFrom().equals(target))) {
            chatService.createRoom(me, target);
        }
    }

    public List<MemberResponse> listWhoLikeMe(Principal principal) {
        Member member = memberRepository.findMemberByEmail(principal.getName()).orElseThrow();
        List<MemberLike> memberLikes = memberLikeRepository.findMemberLikesByLikeTo(member);
        List<Member> membersWhoLikeMe = memberLikes.stream().map(MemberLike::getLikeFrom).collect(Collectors.toList());
        return membersWhoLikeMe.stream().map(MemberResponse::new).collect(Collectors.toList());
    }

    public Slice<MemberRecommendResponse> recommend(Principal principal, Pageable pageable) {
        Member member = memberRepository.findMemberByEmail(principal.getName()).orElseThrow();
        Post post = member.getPost();

        if (post == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "POST_NOT_EXIST");
        }


        Geolocation geolocation = member.getGeolocation();
        if (geolocation == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "GEOLOCATION_NOT_EXIST");
        }

        Slice<Member> memberPage = memberRepository.
                findByDistance(member.getGeolocation().getLatitude(), member.getGeolocation().getLongitude(), pageable);

        return memberPage.map(m -> {
            return new MemberRecommendResponse(m, new PostResponse(m.getPost()));
        });
    }

    //토큰 재발급
    public ResponseEntity<TokenDto> refresh(String refreshToken) {
        if (!jwtTokenProvider.validateRefreshToken(refreshToken)) {
            throw new RuntimeException("Refresh Token이 유효하지 않습니다.");
        }

        Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);
        String jwt = jwtTokenProvider.createToken(authentication);

        if (jwtTokenProvider.isExpirationApproaching(refreshToken)) { //refreshToken 만료 2일 전
            //refreshToken 재발급
            refreshToken = jwtTokenProvider.createRefreshToken(authentication);
        }

        return ResponseEntity.ok(new TokenDto(jwt, refreshToken));
    }

    public void logout(Principal principal) {
        refreshTokenRepository.deleteRefreshTokenByEmail(principal.getName());
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
    //Geolocation

    public void geolocation(Principal principal, GeolocationDto geolocationDto) {
        Member member = memberRepository.findMemberByEmail(principal.getName()).orElseThrow();
        if (member.getGeolocation() == null) {
            Geolocation geolocation = Geolocation.builder()
                    .latitude(geolocationDto.getLatitude())
                    .longitude(geolocationDto.getLongitude())
                    .member(member)
                    .build();
            geolocationRepository.save(geolocation);
        } else {
            Geolocation geolocation = member.getGeolocation();
            geolocation.update(geolocationDto.getLatitude(), geolocationDto.getLongitude());
        }
    }
}

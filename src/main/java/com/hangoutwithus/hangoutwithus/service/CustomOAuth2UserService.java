package com.hangoutwithus.hangoutwithus.service;

import com.hangoutwithus.hangoutwithus.config.oauth.OAuth2Attributes;
import com.hangoutwithus.hangoutwithus.entity.Member;
import com.hangoutwithus.hangoutwithus.entity.Role;
import com.hangoutwithus.hangoutwithus.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;

    public CustomOAuth2UserService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();

        OAuth2User oauth2User = oAuth2UserService.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();
        log.info("registrationId : " + registrationId);
        log.info("userNameAttributeName : " + userNameAttributeName);

        OAuth2Attributes oAuth2Attribute =
                OAuth2Attributes.of(registrationId, userNameAttributeName, oauth2User.getAttributes());

        var user = oAuth2Attribute.convertToMap();
        log.info("user : " + user);
        Member member;

        if (memberRepository.findMemberByEmail(user.get("email").toString()).isPresent()) {
            log.info("이미 가입된 회원입니다.");

            //TODO: 로그인 처리
            member = memberRepository.findMemberByEmail(user.get("email").toString()).get();
        } else {
            //TODO: 회원가입 처리
            log.info("회원가입을 진행합니다.");
            member = Member.builder()
                    .email(user.get("email").toString())
                    .name(user.get("name").toString())
                    .role(Role.ROLE_USER)
                    .isCompletedSignup(false)
                    .build();
            memberRepository.save(member);
        }
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(member.getRole().value())),
                oAuth2Attribute.getAttributes(),
                "email"
        );
    }
}

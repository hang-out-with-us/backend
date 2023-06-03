package com.hangoutwithus.hangoutwithus.config.oauth;

import com.hangoutwithus.hangoutwithus.dto.TokenDto;
import com.hangoutwithus.hangoutwithus.entity.Member;
import com.hangoutwithus.hangoutwithus.jwt.JwtTokenProvider;
import com.hangoutwithus.hangoutwithus.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j

public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public OAuth2SuccessHandler(JwtTokenProvider jwtTokenProvider, MemberRepository memberRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        String token = jwtTokenProvider.createToken(authentication);
        String refreshToken = jwtTokenProvider.createRefreshToken(authentication);
        TokenDto tokenDto = new TokenDto(token, refreshToken);
        Member member = memberRepository.findMemberByEmail(authentication.getName()).orElseThrow();

        String redirectUrl = "hangoutwithus://";

        if(!member.getIsCompletedSignup()) {
            redirectUrl += "?state=signup_not_completed";
        }else{
            redirectUrl += "?state=ok";
        }

        redirectUrl += "&token=" + token;
        redirectUrl += "&refreshToken=" + refreshToken;


        log.info("redirectUrl: {}", redirectUrl);

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }

}
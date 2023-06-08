package com.hangoutwithus.hangoutwithus.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String REFRESH_TOKEN_HEADER = "RefreshToken";
    private final JwtTokenProvider jwtTokenProvider;

    public JwtFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = resolveToken(request);
        String refreshToken = request.getHeader(REFRESH_TOKEN_HEADER);
        String requestURI = request.getRequestURI();

        log.info("requestURI: {}", requestURI);
        log.info("jwt: {}", jwt);
        log.info("refreshToken: {}", refreshToken);

        TokenValidState validState = jwtTokenProvider.authentication(jwt, refreshToken);

        if(request.getRequestURI().startsWith("/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        if((jwt == null || jwt.isEmpty()) && (refreshToken == null || refreshToken.isEmpty())) {
            log.info("JWT 토큰이 없습니다. uri: {}", requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        if (validState == TokenValidState.VALIDATED) {
            //refreshToken과 jwt 토큰이 모두 유효한 경우
            Authentication authentication = jwtTokenProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("Security Context에 '{}' 인증 정보를 저장했습니다. uri: {}", authentication.getName(), requestURI);
            filterChain.doFilter(request, response);
        }
        else if (validState == TokenValidState.EXPIRED) {
            //refreshToken이 유효하고, jwt 토큰이 만료된 경우
            log.info("JWT 토큰이 만료되었습니다. uri: {}", requestURI);
            response.getWriter().write("EXPIRED_ACCESS_TOKEN");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
        else {
            //refreshToken이 유효하지 않거나, jwt 토큰이 유효하지 않은 경우
            log.info("유효한 JWT 토큰이 없습니다. uri: {}", requestURI);
            response.getWriter().write("INVALID_TOKEN");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
    }

    //Request Header에서 토큰 정보를 추출
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}

package com.hangoutwithus.hangoutwithus.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
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
        String requestURI = request.getRequestURI();

        if (request.getRequestURI().startsWith("/auth")) {
            filterChain.doFilter(request, response);
            return;
        }
        TokenValidState tokenValidState = jwtTokenProvider.validateToken(jwt);

        if (tokenValidState == TokenValidState.EXPIRED) {
            log.info("JWT 토큰이 만료되었습니다. uri: {}", requestURI);
            response.getWriter().write("EXPIRED_TOKEN");
        } else if (tokenValidState == TokenValidState.INVALID) {
            log.info("유효하지 않은 JWT 토큰입니다. uri: {}", requestURI);
            jwtTokenProvider.deleteRefreshToken(jwtTokenProvider.getAuthentication(jwt));
            response.getWriter().write("INVALID_TOKEN");
        } else if (tokenValidState == TokenValidState.VALIDATED) {
            Authentication authentication = jwtTokenProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("Security Context에 '{}' 인증 정보를 저장했습니다. uri: {}", authentication.getName(), requestURI);
        }
        filterChain.doFilter(request, response);

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

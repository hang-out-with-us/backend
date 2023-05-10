package com.hangoutwithus.hangoutwithus.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtFilter extends GenericFilterBean {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String REFRESH_TOKEN_HEADER = "RefreshToken";
    private final JwtTokenProvider jwtTokenProvider;

    public JwtFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String jwt = resolveToken(httpServletRequest);
        String refreshToken = httpServletRequest.getHeader(REFRESH_TOKEN_HEADER);
        String requestURI = httpServletRequest.getRequestURI();
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        
        TokenValidState accessTokenValidState = jwtTokenProvider.validateToken(jwt);
        TokenValidState refreshTokenValidState = jwtTokenProvider.validateToken(refreshToken);

        if (refreshTokenValidState == TokenValidState.VALIDATED &&
                accessTokenValidState == TokenValidState.VALIDATED) {
            //refreshToken과 jwt 토큰이 모두 유효한 경우
            Authentication authentication = jwtTokenProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("Security Context에 '{}' 인증 정보를 저장했습니다. uri: {}", authentication.getName(), requestURI);
        }
        else if (refreshTokenValidState == TokenValidState.VALIDATED &&
                accessTokenValidState == TokenValidState.EXPIRED) {
            //refreshToken이 유효하고, jwt 토큰이 만료된 경우
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "EXPIRED_ACCESS_TOKEN");
        }
        else {
            //refreshToken이 유효하지 않거나, jwt 토큰이 유효하지 않은 경우
            log.debug("유효한 JWT 토큰이 없습니다. uri: {}", requestURI);
        }

        chain.doFilter(request, httpServletResponse);
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

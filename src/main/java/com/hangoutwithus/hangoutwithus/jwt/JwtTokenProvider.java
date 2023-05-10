package com.hangoutwithus.hangoutwithus.jwt;


import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {
    private static final String AUTHORITIES_KEY = "auth";
    private final String secret;
    private final long tokenValidityInMilliseconds;
    private final long refreshTokenValidityInMilliseconds;

    private Key key;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.token_validity_in_seconds}") long tokenValidityInSeconds) {
        this.secret = secret;
        this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000;
        this.refreshTokenValidityInMilliseconds = 1000 * 60 * 60 * 24 * 7;
    }

    //빈이 생성 되고 주입을 받은 후에 secret값을 Base64 Decode해서 key변수에 할당
    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    //Authentication객체의 권한정보를 이용해 토큰을 생성
    public String createToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        //토큰 유효기간 설정
        long now = (new Date()).getTime();
        Date validity = new Date(now + this.tokenValidityInMilliseconds);

        return Jwts.builder()
                .setSubject(authentication.getName())   //payload "sub": "name"
                .claim(AUTHORITIES_KEY, authorities)    //payload "auth": "ROLE_USER"
                .setExpiration(validity)                //payload "exp": "38400"
                .signWith(key, SignatureAlgorithm.HS256)//header "alg": "HS256"
                .compact();
    }

    public String createRefreshToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        //토큰 유효기간 설정
        long now = (new Date()).getTime();
        Date validity = new Date(now + this.refreshTokenValidityInMilliseconds);

        return Jwts.builder()
                .setSubject(authentication.getName())   //payload "sub": "name"
                .claim(AUTHORITIES_KEY, authorities)    //payload "auth": "ROLE_USER"
                .setExpiration(validity)                //payload "exp": "38400"
                .signWith(key, SignatureAlgorithm.HS256)//header "alg": "HS256"
                .compact();
    }
    //token -> Authentication
    public Authentication getAuthentication(String token) {

        //token을 이용해 Claim을 만들어 준다.
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        //Claim에서 권한정보들을 빼내어 User객체를 만든다
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        //User 객체, 토큰, 권한정보를 이용해 Authentication 객체를 반환한다.
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    //유효기간 임박 여부
    public boolean isExpirationApproaching(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        //2일 남았으면 true 반환
        return claims.getExpiration().getTime() - System.currentTimeMillis() < 1000 * 60 * 60 * 24 * 2;
    }


    //토큰 유효성 검증
    public TokenValidState validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return TokenValidState.VALIDATED;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
            return TokenValidState.INVALID;
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
            return TokenValidState.EXPIRED;
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
            return TokenValidState.INVALID;
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
            return TokenValidState.INVALID;
        }
    }
}
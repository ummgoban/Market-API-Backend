package com.market.core.security.jwt;

import com.market.core.security.service.PrincipalDetails;
import com.market.core.security.service.PrincipalDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-expiration-seconds}")
    private long accessExpirationSeconds;

    @Value("${jwt.refresh-expiration-seconds}")
    private long refreshExpirationSeconds;

    private final PrincipalDetailsService principalDetailsService;

    /**
     * Access 토큰 생성
     */
    public String createAccessToken(PrincipalDetails principalDetails) {
        return createToken(principalDetails, accessExpirationSeconds);
    }

    /**
     * Refresh 토큰 생성
     */
    public String createRefreshToken(PrincipalDetails principalDetails) {
        return createToken(principalDetails, refreshExpirationSeconds);
    }

    /**
     * JWT 토큰 생성 공통 로직
     */
    private String createToken(PrincipalDetails principalDetails, long expirationTime) {
        // 토큰 생성에 사용할 변수들 정의
        String subject = principalDetails.getUsername();
        Date issuedAt = Date.from(Instant.now());
        Date expiration = Date.from(Instant.now().plusSeconds(expirationTime));
        Long userId = principalDetails.getId(); // 사용자 ID
        String roles = (principalDetails.getAuthorities() == null || principalDetails.getAuthorities().isEmpty()) ? "" :
                principalDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(","));

        // JWT 토큰 생성
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .claim("id", userId)
                .claim("roles", roles)
                .signWith(extractSecretKey(), SignatureAlgorithm.HS512) // 서명에 사용할 비밀 키
                .compact(); // JWT 문자열로 생성
    }

    /**
     * JWT 토큰을 기반으로 사용자 인증 객체를 생성
     */
    public Authentication createAuthentication(String token) {
        // JWT 토큰을 파싱하고 클레임(Claims) 추출
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(extractSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        // 권한 정보 추출
        String roles = claims.get("roles", String.class);
        List<GrantedAuthority> authorities = (roles != null && !roles.trim().isEmpty()) ?
                List.of(new SimpleGrantedAuthority(roles)) : List.of();

        // 사용자 정보를 담은 UserDetails 객체 생성
        UserDetails user = PrincipalDetails.builder()
                .id(claims.get("id", Long.class))
                .email(claims.getSubject())
                .password(null)
                .authorities(authorities)
                .build();

        // Authentication 객체 생성하여 반환
        return new UsernamePasswordAuthenticationToken(user, token, authorities);
    }

    /**
     * 토큰 유효성 검증
     */
    public void validateToken(String token) {
        Jwts.parserBuilder()
                .setSigningKey(extractSecretKey())
                .build()
                .parseClaimsJws(token);
    }

    /**
     * 권한 비교
     */
    public void compareAuthorities(Authentication authentication) throws AccessDeniedException {
        // JWT 토큰의 권한
        Set<String> tokenAuthorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        // 데이터베이스에 저장된 사용자 권한
        UserDetails userDetails = principalDetailsService.loadUserByUsername(authentication.getName());
        Set<String> dbAuthorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        // 권한 비교
        if (!tokenAuthorities.containsAll(dbAuthorities)) {
            throw new AccessDeniedException("JWT 토큰의 권한과 데이터베이스 권한이 일치하지 않습니다.");
        }
    }

    /**
     * SecretKey 추출
     */
    public SecretKey extractSecretKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }
}
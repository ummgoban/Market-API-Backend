package com.market.core.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.market.core.code.error.BaseErrorCode;
import com.market.core.code.error.JwtErrorCode;
import com.market.core.security.service.jwt.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

import static org.springframework.util.StringUtils.hasText;

/**
 * 클라이언트 요청에서 JWT 토큰을 추출하고 검증하여 인증된 사용자의 접근을 허용하는 필터 클래스입니다.
 */
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private static final String AUTHENTICATION_HEADER = "Authorization";
    private static final String AUTHENTICATION_SCHEME = "Bearer ";
    private static final String APPLICATION_JSON = "application/json";
    private static final String UTF_8 = "UTF-8";

    private final JwtService jwtService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = extractToken(request);
            if (hasText(token)) {
                Authentication authentication = jwtService.createAuthentication(token);

                // 1. 토큰 유효성 검증
                jwtService.validateToken(token);

                // 2. 권한 비교
                jwtService.compareAuthorities(authentication);

                // SecurityContext 에 인증 객체 설정
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(authentication);

                SecurityContextHolder.setContext(context);
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            // 예외 처리
            BaseErrorCode errorCode = determineErrorCode(e);

            response.setStatus(errorCode.getStatus().value()); // 상태 코드 설정
            response.setContentType(APPLICATION_JSON); // 응답의 Content-Type 설정
            response.setCharacterEncoding(UTF_8); // 문자 인코딩 설정

            // JSON 직렬화
            String responseMessage = objectMapper.writeValueAsString(errorCode.getErrorResponse());
            response.getWriter().write(responseMessage);
        }
    }

    /**
     * 예외에 따른 오류 코드 설정
     */
    private BaseErrorCode determineErrorCode(Exception e) {
        if (e instanceof ExpiredJwtException) {
            // 토큰 만료
            return JwtErrorCode.VALIDATION_TOKEN_EXPIRED;
        } else if (e instanceof AuthenticationCredentialsNotFoundException) {
            // 유효하지 않은 토큰
            return JwtErrorCode.VALIDATION_TOKEN_FAILED;
        } else if (e instanceof AccessDeniedException) {
            // 접근 권한이 없음
            return JwtErrorCode.VALIDATION_TOKEN_NOT_AUTHORIZATION;
        } else {
            // 그 외의 에러
            return JwtErrorCode.VALIDATION_TOKEN_FAILED;
        }
    }

    /**
     * JWT 토큰 추출
     */
    public String extractToken(HttpServletRequest request) {
        String token = request.getHeader(AUTHENTICATION_HEADER);

        if (hasText(token) && token.startsWith(AUTHENTICATION_SCHEME)) {
            return token.substring(AUTHENTICATION_SCHEME.length());
        }

        throw new AuthenticationCredentialsNotFoundException("토큰이 존재하지 않습니다.");
    }
}
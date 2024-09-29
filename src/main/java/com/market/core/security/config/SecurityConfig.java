package com.market.core.security.config;

import com.market.core.security.filter.JwtFilter;
import com.market.core.security.service.jwt.JwtService;
import com.market.member.entity.RolesType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.List;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

/**
 * JWT 및 OAuth 관련 설정을 포함하고 있는 Spring Security 설정 클래스입니다.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    /**
     * 로그인 인증 작업 처리
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * 공개 접근 필터 체인
     */
    @Bean
    @Order(1)
    public SecurityFilterChain publicFilterChain(HttpSecurity http) throws Exception {
        defaultSecuritySetting(http);
        http
                .securityMatchers(matcher -> matcher
                        .requestMatchers(publicRequestMatchers()))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(publicRequestMatchers()).permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    /**
     * 인증 및 권한이 필요한 필터 체인
     */
    @Bean
    @Order(2)
    public SecurityFilterChain authenticatedFilterChain(HttpSecurity http, JwtService jwtService) throws Exception {
        defaultSecuritySetting(http);
        http
                .securityMatchers(matcher -> matcher
                        .requestMatchers(authenticatedRequestMatchers()))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(authenticatedRequestMatchers())
                        .hasAnyAuthority(RolesType.ROLE_USER.name(), RolesType.ROLE_STORE_OWNER.name())
                        .anyRequest().authenticated())
//                  TODO: 예외 처리
//                .exceptionHandling(exception -> exception
//                        .accessDeniedHandler())
                .addFilterBefore(new JwtFilter(jwtService), ExceptionTranslationFilter.class);

        return http.build();
    }

    /**
     * 공개 접근 endpoint
     */
    private RequestMatcher[] publicRequestMatchers() {
        List<RequestMatcher> requestMatchers = List.of(
                antMatcher(GET, "/swagger-ui/**"), // Swagger UI 웹 인터페이스를 제공하는 경로
                antMatcher(GET, "/v3/api-docs/**"), // Swagger의 API 문서 데이터를 JSON 형식으로 제공하는 경로
                antMatcher(POST, "/auth/accessToken"), // OAuth AccessToken 발급
                antMatcher(POST, "/auth/login"), // OAuth 로그인
                antMatcher(POST, "/auth/refresh"), // 토큰 갱신
                antMatcher(GET, "/market/{marketId}"), // 가게 상세 조회
                antMatcher(GET, "/market/business/validate"), // 사업자 등록 번호 유효성 검증
                antMatcher(GET, "/market/paging") // 가게 목록
        );

        return requestMatchers.toArray(RequestMatcher[]::new);
    }

    /**
     * 인증 및 권한이 필요한 endpoint
     */
    private RequestMatcher[] authenticatedRequestMatchers() {
        List<RequestMatcher> requestMatchers = List.of(
                antMatcher(POST, "/market"), // 가게 등록
                antMatcher(PATCH, "/market/{marketId}/hours") // 가게 영업 시간 및 픽업 시간 설정
        );

        return requestMatchers.toArray(RequestMatcher[]::new);
    }

    /**
     * Spring Security 기본 설정 메서드
     */
    private void defaultSecuritySetting(HttpSecurity http) throws Exception {
        http
                // JWT, OAuth 기반
                .csrf(AbstractHttpConfigurer::disable) // CSRF 보호 비활성화
                .cors(AbstractHttpConfigurer::disable) // CORS 설정 비활성화 (React Native)
                .formLogin(AbstractHttpConfigurer::disable) // Form 기반 로그인 비활성화
                .httpBasic(AbstractHttpConfigurer::disable) // HTTP Basic 인증 비활성화
                .rememberMe(AbstractHttpConfigurer::disable) // 세션 기반의 인증 비활성화
                .logout(AbstractHttpConfigurer::disable) // 로그아웃 기능 비활성화
                .anonymous(AbstractHttpConfigurer::disable) // 익명 사용자 접근 비할성화
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 생성하지 않음
                .headers(headers -> headers.frameOptions(
                        HeadersConfigurer.FrameOptionsConfig::disable) // X-Frame-Options 헤더 비활성화, 클릭재킹 공격 방지
                );
    }
}
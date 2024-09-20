package com.market.core.security.config;

import com.market.core.security.filter.JwtFilter;
import com.market.core.security.service.jwt.JwtService;
import com.market.core.security.service.oauth.CustomAuthorizationRequestResolver;
import com.market.member.entity.RolesType;
import com.market.core.security.service.oauth.CustomOAuth2FailureHandler;
import com.market.core.security.service.oauth.CustomOAuth2SuccessHandler;
import com.market.core.security.service.oauth.CustomOAuth2UserService;
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
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

/**
 * JWT 및 OAuth2 관련 설정을 포함하고 있는 Spring Security 설정 클래스입니다.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomOAuth2SuccessHandler customOAuth2SuccessHandler;
    private final CustomOAuth2FailureHandler customOAuth2FailureHandler;
    private final ClientRegistrationRepository clientRegistrationRepository;

    /**
     * API 커스텀(쿼리 파라미터)을 위한 Resolver
     */
    @Bean
    public CustomAuthorizationRequestResolver customAuthorizationRequestResolver() {
        return new CustomAuthorizationRequestResolver(clientRegistrationRepository);
    }

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
//                        TODO: endpoint 추가 후 주석 풀기
//                        .anyRequest().authenticated()
                        .anyRequest().permitAll()
                );

        return http.build();
    }

    /**
     * 인증 및 권한이 필요한 필터 체인
     */
    @Bean
    @Order(2)
    public SecurityFilterChain authenticatedFilterChain(HttpSecurity http, JwtService jwtProvider) throws Exception {
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
                .addFilterBefore(new JwtFilter(jwtProvider), ExceptionTranslationFilter.class);

        return http.build();
    }

    /**
     * 공개 접근 endpoint
     */
    private RequestMatcher[] publicRequestMatchers() {
        List<RequestMatcher> requestMatchers = List.of(
//                TODO: endpoint 추가
                antMatcher("/market/paging"),
                antMatcher("/swagger/**")
        );

        return requestMatchers.toArray(RequestMatcher[]::new);
    }

    /**
     * 인증 및 권한이 필요한 endpoint
     */
    private RequestMatcher[] authenticatedRequestMatchers() {
        List<RequestMatcher> requestMatchers = List.of(
//                TODO: endpoint 추가
                antMatcher("/**")
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
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS 설정
                .formLogin(AbstractHttpConfigurer::disable) // Form 기반 로그인 비활성화
                .httpBasic(AbstractHttpConfigurer::disable) // HTTP Basic 인증 비활성화
                .rememberMe(AbstractHttpConfigurer::disable) // 세션 기반의 인증 비활성화
                .logout(AbstractHttpConfigurer::disable) // 로그아웃 기능 비활성화
                .anonymous(AbstractHttpConfigurer::disable) // 익명 사용자 접근 비할성화
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 생성하지 않음
                .headers(headers -> headers.frameOptions(
                        HeadersConfigurer.FrameOptionsConfig::disable) // X-Frame-Options 헤더 비활성화, 클릭재킹 공격 방지
                )
                .oauth2Login(oauth -> oauth
                        .successHandler(customOAuth2SuccessHandler)
                        .failureHandler(customOAuth2FailureHandler)
                        .userInfoEndpoint(userinfo -> userinfo
                                .userService(customOAuth2UserService))
                        .authorizationEndpoint(endpoint -> endpoint
                                .authorizationRequestResolver(customAuthorizationRequestResolver()))
                ); // OAuth2 로그인 설정
    }

    /**
     * CORS 설정
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 허용할 Origin(출처)
//        TODO: 허용할 Origin 설정
        configuration.setAllowedOriginPatterns(List.of("*"));
//        configuration.setAllowedOrigins(
//                Arrays.asList(
//                        "http://localhost:8080",
//                        "http://127.0.0.1:8080",
//                        "http://localhost:8080/swagger-ui.html",
//                        "http://127.0.0.1:8080/swagger-ui.html"
//
//                )
//        );

        // 허용할 HTTP 메서드
        configuration.setAllowedMethods(
                Arrays.asList(
                        "GET",
                        "POST",
                        "PUT",
                        "PATCH",
                        "DELETE"
                )
        );

        // 허용할 헤더
//        TODO: 허용할 헤더 추가
        configuration.setAllowedHeaders(List.of("*"));
//        configuration.setAllowedHeaders(
//                Arrays.asList(
//                        "Authorization",
//                        "Cache-Control",
//                        "Content-Type",
//                        "X-Requested-With"
//                )
//        );

        // 인증 정보(쿠키, Authorization 헤더 등)를 포함한 요청 허용
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 경로에 대해 CORS 설정 적용

        return source;
    }
}
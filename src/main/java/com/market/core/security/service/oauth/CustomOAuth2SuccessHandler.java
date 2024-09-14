package com.market.core.security.service.oauth;

import com.market.core.code.error.MemberErrorCode;
import com.market.core.exception.MemberException;
import com.market.core.security.dto.oauth.CustomOAuth2User;
import com.market.core.security.dto.jwt.JwtTokenDto;
import com.market.core.security.service.jwt.JwtService;
import com.market.member.dto.request.MemberLoginDto;
import com.market.member.entity.RolesType;
import com.market.member.service.MemberService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * CustomOAuth2UserService 인증 성공 시 처리하는 핸들러 클래스입니다.
 */
@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final MemberService memberService;
    private final JwtService jwtService;

    /**
     * OAuth2 회원가입 및 로그인
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        // 권한 정보 가져오기
        GrantedAuthority authority = customOAuth2User.getAuthorities().stream().findFirst()
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER_ROLES));
        RolesType role = RolesType.valueOf(authority.getAuthority());

        // CustomOAuth2User를 MemberLoginDto로 변환
        MemberLoginDto memberLoginDto = MemberLoginDto.builder()
                .oauthId(customOAuth2User.getOauthId())
                .provider(customOAuth2User.getProvider())
                .name(customOAuth2User.getName())
                .profileImageUrl(customOAuth2User.getProfileImageUrl())
                .role(role)
                .build();

        // 로그인 처리 및 JWT 토큰 생성
        JwtTokenDto jwtTokens = memberService.login(memberLoginDto);

        // JWT 토큰을 응답 헤더에 추가
        jwtService.setAccessTokenToHeader(response, jwtTokens.getAccessToken());
        jwtService.setRefreshTokenToHeader(response, jwtTokens.getRefreshToken());
    }
}
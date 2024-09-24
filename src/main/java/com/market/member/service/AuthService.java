package com.market.member.service;

import com.market.core.code.error.OAuthErrorCode;
import com.market.core.exception.OAuthException;
import com.market.core.security.dto.jwt.AccessTokenResponse;
import com.market.core.security.dto.jwt.JwtTokenResponse;
import com.market.core.security.service.jwt.JwtService;
import com.market.core.security.service.oauth.KakaoOAuthService;
import com.market.core.security.service.oauth.NaverOAuthService;
import com.market.core.security.service.oauth.OAuthService;
import com.market.member.dto.server.MemberJwtDto;
import com.market.member.dto.server.MemberLoginDto;
import com.market.member.dto.request.OAuthAuthorizationRequest;
import com.market.member.dto.request.OAuthLoginRequest;
import com.market.member.entity.Member;
import com.market.member.entity.ProviderType;
import com.market.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtService jwtService;
    private final KakaoOAuthService kakaoOAuthService;
    private final NaverOAuthService naverOAuthService;

    /**
     * Provider 서버에서 AccessToken 발급
     * (백엔드 테스트 용도)
     */
    public AccessTokenResponse getAccessToken(OAuthAuthorizationRequest oAuthAuthorizationRequest) {
        OAuthService oAuthService = getOAuthService(oAuthAuthorizationRequest.getProvider());
        String accessToken = oAuthService.getAccessToken(oAuthAuthorizationRequest);

        return AccessTokenResponse.builder()
                .accessToken(accessToken)
                .build();
    }

    /**
     * OAuth 로그인
     */
    public JwtTokenResponse login(OAuthLoginRequest oAuthLoginRequest) {
        OAuthService oAuthService = getOAuthService(oAuthLoginRequest.getProvider());

        // OAuth 제공자로부터 회원 정보 조회
        MemberLoginDto memberLoginDto = oAuthService.getUserInfo(oAuthLoginRequest);

        // DB 회원 정보 조회
        Member member = memberRepository.findByOauthIdAndRoles(memberLoginDto.getOauthId(), memberLoginDto.getRoles())
                .orElseGet(() -> registerMember(memberLoginDto)); // 회원 정보가 없을 경우 회원가입

        MemberJwtDto memberJwtDto = new MemberJwtDto(member);

        // JWT 토큰 생성 및 반환
        return createAccessTokenAndRefreshToken(memberJwtDto);
    }

    /**
     * OAuth 신규 회원 저장
     */
    private Member registerMember(MemberLoginDto memberLoginDto) {
        Member newMember = Member.builder()
                .oauthId(memberLoginDto.getOauthId())
                .provider(memberLoginDto.getProvider())
                .name(memberLoginDto.getName())
                .profileImageUrl(memberLoginDto.getProfileImageUrl())
                .roles(memberLoginDto.getRoles())
                .build();

        return memberRepository.save(newMember);
    }

    /**
     * Access Token 및 Refresh Token 발급
     */
    private JwtTokenResponse createAccessTokenAndRefreshToken(MemberJwtDto memberJwtDto) {
        String accessToken = jwtService.createAccessToken(memberJwtDto);
        String refreshToken = jwtService.createRefreshToken(memberJwtDto);

        return JwtTokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * OAuth 제공자 서비스 반환
     */
    private OAuthService getOAuthService(ProviderType provider) {
        return switch (provider) {
            case NAVER -> naverOAuthService;
            case KAKAO -> kakaoOAuthService;
            default -> throw new OAuthException(OAuthErrorCode.UNSUPPORTED_OAUTH_PROVIDER);
        };
    }
}
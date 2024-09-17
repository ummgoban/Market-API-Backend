package com.market.core.security.service.oauth;

import com.market.core.code.error.OAuthErrorCode;
import com.market.core.exception.OAuthException;
import com.market.core.security.dto.oauth.NaverUserInfoDto;
import com.market.member.dto.request.MemberLoginDto;
import com.market.member.dto.request.OAuthAuthorizationDto;
import com.market.member.dto.request.OAuthLoginDto;
import com.market.core.security.dto.oauth.NaverResponseDto;
import com.market.member.entity.ProviderType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Naver OAuth 인증을 통해 액세스 토큰 발급 및 사용자 정보를 조회하는 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class NaverOAuthService implements OAuthService {

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.provider.naver.user-info-uri}")
    private String baseUri;

    @Value("${spring.security.oauth2.client.provider.naver.token-uri}")
    private String tokenUri;

    private final String contentType = "application/x-www-form-urlencoded;charset=utf-8";

    /**
     * OAuth 인증 코드로 액세스 토큰을 발급받는 메서드 (백엔드 테스트 용도)
     */
    @Override
    public String getAccessToken(OAuthAuthorizationDto oAuth2AuthorizationDto) {

        WebClient webClient = WebClient.builder()
                .baseUrl(tokenUri)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, contentType)
                .build();

        NaverResponseDto response = webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", clientId)
                        .queryParam("client_secret", clientSecret)
                        .queryParam("code", oAuth2AuthorizationDto.getCode())
                        .queryParam("state", oAuth2AuthorizationDto.getState())
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, responseEntity -> Mono.error(new OAuthException(OAuthErrorCode.BAD_REQUEST_OAUTH_TOKEN)))
                .onStatus(HttpStatusCode::is5xxServerError, responseEntity -> Mono.error(new OAuthException(OAuthErrorCode.OAUTH_PROVIDER_SERVER_ERROR)))
                .bodyToMono(NaverResponseDto.class)
                .block();

        if (response == null || response.getAccessToken() == null) {
            throw new OAuthException(OAuthErrorCode.INVALID_ACCESS_TOKEN);
        }

        return response.getAccessToken();
    }

    /**
     * OAuth 제공자에서 사용자 정보를 가져와 MemberLoginDto로 변환
     */
    @Override
    public MemberLoginDto getUserInfo(OAuthLoginDto oAuthLoginDto) {

        WebClient webClient = WebClient.builder()
                .baseUrl(baseUri)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + oAuthLoginDto.getAccessToken())
                .build();

        NaverUserInfoDto userinfo = webClient.get()
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.error(new OAuthException(OAuthErrorCode.BAD_REQUEST_OAUTH_USER_INFO)))
                .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.error(new OAuthException(OAuthErrorCode.OAUTH_PROVIDER_SERVER_ERROR)))
                .bodyToMono(NaverUserInfoDto.class)
                .block();

        if (userinfo == null || userinfo.getResponse() == null) {
            throw new OAuthException(OAuthErrorCode.INVALID_USER_INFO);
        }

        return MemberLoginDto.builder()
                .oauthId(userinfo.getResponse().getOauthId())
                .provider(ProviderType.NAVER)
                .name(userinfo.getResponse().getName())
                .profileImageUrl(userinfo.getResponse().getProfileImageUrl())
                .roles(oAuthLoginDto.getRoles())
                .build();
    }
}
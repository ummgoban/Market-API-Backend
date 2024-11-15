package com.market.core.security.service.oauth;

import com.market.core.code.error.OAuthErrorCode;
import com.market.core.exception.OAuthException;
import com.market.core.security.dto.jwt.server.KakaoErrorResponseDto;
import com.market.core.security.dto.oauth.KakaoUserInfoDto;
import com.market.member.dto.server.MemberLoginDto;
import com.market.member.dto.request.OAuthAuthorizationRequest;
import com.market.member.dto.request.OAuthLoginRequest;
import com.market.core.security.dto.oauth.KakaoResponseDto;
import com.market.member.entity.ProviderType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Kakao OAuth 인증을 통해 액세스 토큰 발급 및 사용자 정보를 조회하는 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class KakaoOAuthService implements OAuthService {

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;

    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String baseUri;

    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String tokenUri;

    private final String contentType = "application/x-www-form-urlencoded;charset=utf-8";

    /**
     * OAuth 인증 코드로 액세스 토큰을 발급받는 메서드 (백엔드 테스트 용도)
     */
    @Override
    public String getAccessToken(OAuthAuthorizationRequest oAuthAuthorizationRequest) {

        WebClient webClient = WebClient.builder()
                .baseUrl(tokenUri)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, contentType)
                .build();

        KakaoResponseDto response = webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", clientId)
                        .queryParam("redirect_uri", redirectUri)
                        .queryParam("code", oAuthAuthorizationRequest.getCode())
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, responseEntity -> Mono.error(new OAuthException(OAuthErrorCode.BAD_REQUEST_OAUTH_USER_INFO)))
                .onStatus(HttpStatusCode::is5xxServerError, responseEntity -> Mono.error(new OAuthException(OAuthErrorCode.OAUTH_PROVIDER_SERVER_ERROR)))
                .bodyToMono(KakaoResponseDto.class)
                .block();

        return response.getAccessToken();
    }

    /**
     * OAuth 제공자에서 사용자 정보를 가져와 MemberLoginDto로 변환
     */
    public MemberLoginDto getUserInfo(OAuthLoginRequest oAuthLoginRequest) {

        WebClient webClient = WebClient.builder()
                .baseUrl(baseUri)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, contentType)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + oAuthLoginRequest.getAccessToken())
                .build();

        KakaoUserInfoDto userinfo = webClient.post()
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> handleClientError(clientResponse))
                .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.error(new OAuthException(OAuthErrorCode.OAUTH_PROVIDER_SERVER_ERROR)))
                .bodyToMono(KakaoUserInfoDto.class)
                .block();

        return MemberLoginDto.builder()
                .oauthId(String.valueOf(userinfo.getOauthId()))
                .provider(ProviderType.KAKAO)
                .name(userinfo.getKakaoAccount().getProfile().getName())
                .profileImageUrl(userinfo.getKakaoAccount().getProfile().getProfileImageUrl())
                .roles(oAuthLoginRequest.getRoles())
                .build();
    }

    /**
     * OAuth 클라이언트 오류 처리 메서드
     */
    private Mono<? extends Throwable> handleClientError(ClientResponse clientResponse) {
        return clientResponse.bodyToMono(KakaoErrorResponseDto.class)
                .flatMap(errorResponse -> {
                    String errorCode = errorResponse.getErrorCode();
                    return switch (errorCode) {
                        case "KOE101" -> Mono.error(new OAuthException(OAuthErrorCode.INVALID_CLIENT));
                        case "KOE320" -> Mono.error(new OAuthException(OAuthErrorCode.INVALID_TOKEN));
                        case "KOE401" -> Mono.error(new OAuthException(OAuthErrorCode.UNAUTHORIZED));
                        default -> Mono.error(new OAuthException(OAuthErrorCode.BAD_REQUEST_OAUTH_USER_INFO));
                    };
                });
    }
}
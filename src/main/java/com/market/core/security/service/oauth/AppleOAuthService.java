package com.market.core.security.service.oauth;

import com.market.core.code.error.JwtErrorCode;
import com.market.core.exception.JwtException;
import com.market.member.dto.request.OAuthAuthorizationRequest;
import com.market.member.dto.request.OAuthLoginRequest;
import com.market.member.dto.server.MemberLoginDto;
import com.market.member.entity.ProviderType;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.security.interfaces.RSAPublicKey;

/**
 * Apple OAuth 인증을 통해 액세스 토큰 발급 및 사용자 정보를 조회하는 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class AppleOAuthService implements OAuthService {

    private static final String APPLE_JWKS_URL = "https://appleid.apple.com/auth/keys";

    /**
     * Apple OAuth에서는 AccessToken 발급을 직접 처리하지 않으므로 빈 문자열을 반환
     */
    @Override
    public String getAccessToken(OAuthAuthorizationRequest oAuthAuthorizationRequest) {
        return "";
    }

    /**
     * OAuth 제공자에서 사용자 정보를 가져와 MemberLoginDto로 변환
     */
    @Override
    public MemberLoginDto getUserInfo(OAuthLoginRequest oAuthLoginRequest) {
        JWTClaimsSet claims = verifyAndParseIdToken(oAuthLoginRequest.getAccessToken());
        String oauthId = claims.getSubject();

        return MemberLoginDto.builder()
                .oauthId(oauthId)
                .provider(ProviderType.APPLE)
                .roles(oAuthLoginRequest.getRoles())
                .build();
    }

    /**
     * Apple ID Token 검증 및 사용자 정보 파싱
     */
    private JWTClaimsSet verifyAndParseIdToken(String idToken) {
        try {
            SignedJWT signedJWT = parseJwt(idToken);
            RSAPublicKey publicKey = getPublicKey(signedJWT.getHeader().getKeyID());
            verifySignature(signedJWT, publicKey);

            return signedJWT.getJWTClaimsSet();
        } catch (Exception e) {
            throw new JwtException(JwtErrorCode.INVALID_TOKEN);
        }
    }

    /**
     * JWT 문자열 파싱
     */
    private SignedJWT parseJwt(String idToken) throws Exception {
        return SignedJWT.parse(idToken);
    }

    /**
     * Apple JWKS에서 공개 키 가져오기
     */
    private RSAPublicKey getPublicKey(String keyId) throws Exception {
        JWKSet jwkSet = JWKSet.load(new URL(APPLE_JWKS_URL));
        return (RSAPublicKey) jwkSet.getKeyByKeyId(keyId).toRSAKey().toPublicKey();
    }

    /**
     * JWT 서명을 검증
     */
    private void verifySignature(SignedJWT signedJWT, RSAPublicKey publicKey) throws Exception{
        signedJWT.verify(new com.nimbusds.jose.crypto.RSASSAVerifier(publicKey));
    }
}
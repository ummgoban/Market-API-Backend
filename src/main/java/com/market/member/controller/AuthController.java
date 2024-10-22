package com.market.member.controller;

import com.market.core.response.BfResponse;
import com.market.core.security.dto.jwt.request.RefreshTokenRequest;
import com.market.core.security.dto.jwt.response.AccessTokenResponse;
import com.market.core.security.dto.jwt.response.JwtTokenResponse;
import com.market.core.response.ErrorResponse;
import com.market.member.dto.request.OAuthAuthorizationRequest;
import com.market.member.dto.request.OAuthLoginRequest;
import com.market.member.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "인증", description = "인증, 인가 및 토큰 관련 API")
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "OAuth AccessToken 발급",
            description = "OAuth 제공자 서버로부터 AccessToken을 발급받습니다. (백엔드 테스트 용도입니다.)"
    )
    @SecurityRequirements(value = {})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OAuth AccessToken 발급 성공", useReturnTypeSchema = true),
    })
    @PostMapping("/request")
    public ResponseEntity<BfResponse<AccessTokenResponse>> getAccessToken(@RequestBody OAuthAuthorizationRequest oAuthAuthorizationRequest) {
        AccessTokenResponse accessToken = authService.getAccessToken(oAuthAuthorizationRequest);
        return ResponseEntity.ok(new BfResponse<>(accessToken));
    }

    @Operation(
            summary = "OAuth 로그인",
            description = "OAuth 제공자의 자격 증명을 사용하여 로그인하고 JWT 토큰을 발급합니다."
    )
    @SecurityRequirements(value = {})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "JWT 토큰 발급 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "JWT 토큰 발급 시 잘못된 요청으로 인해 반환되는 에러 메시지입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PostMapping("/login")
    public ResponseEntity<BfResponse<JwtTokenResponse>> oAuthLogin(@RequestBody OAuthLoginRequest oAuthLoginRequest) {
        JwtTokenResponse jwtToken = authService.login(oAuthLoginRequest);
        return ResponseEntity.ok(new BfResponse<>(jwtToken));
    }

    @Operation(
            summary = "토큰 갱신",
            description = "기존 Refresh Token을 사용하여 새로운 Access Token과 Refresh Token을 발급받습니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "JWT 토큰 갱신 성공", useReturnTypeSchema = true),
    })
    @PostMapping("/refresh")
    public ResponseEntity<BfResponse<JwtTokenResponse>> refreshTokens(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        JwtTokenResponse jwtTokenDto = authService.refreshTokens(refreshTokenRequest);
        return ResponseEntity.ok(new BfResponse<>(jwtTokenDto));
    }
}
package com.market.member.controller;

import com.market.core.response.BfResponse;
import com.market.core.security.dto.jwt.AccessTokenDto;
import com.market.core.security.dto.jwt.JwtTokenDto;
import com.market.member.dto.request.OAuthAuthorizationDto;
import com.market.member.dto.request.OAuthLoginDto;
import com.market.member.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "인증", description = "사용자 인증, 로그인, 로그아웃 및 토큰 관리 API")
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "OAuth AccessToken 발급",
            description = "OAuth 제공자 서버로부터 AccessToken을 발급받습니다. (백엔드 테스트 용도입니다.)"
    )
    @PostMapping("/accessToken")
    public ResponseEntity<BfResponse<AccessTokenDto>> getAccessToken(@RequestBody OAuthAuthorizationDto oAuthAuthorizationDto) {
        AccessTokenDto accessToken = authService.getAccessToken(oAuthAuthorizationDto);
        return ResponseEntity.ok(new BfResponse<>(accessToken));
    }

    @Operation(
            summary = "OAuth 로그인",
            description = "OAuth 제공자의 자격 증명을 사용하여 로그인하고 JWT 토큰을 발급합니다."
    )
    @PostMapping("/login")
    public ResponseEntity<BfResponse<JwtTokenDto>> oAuthLogin(@RequestBody OAuthLoginDto oAuthLoginDto) {
        JwtTokenDto jwtTokenDto = authService.login(oAuthLoginDto);
        return ResponseEntity.ok(new BfResponse<>(jwtTokenDto));
    }
}
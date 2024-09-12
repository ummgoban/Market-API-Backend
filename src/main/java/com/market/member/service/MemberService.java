package com.market.member.service;

import com.market.core.security.service.jwt.JwtService;
import com.market.core.security.dto.jwt.JwtTokenDto;
import com.market.member.dto.request.MemberJwtDto;
import com.market.member.dto.request.MemberLoginDto;
import com.market.member.entity.Member;
import com.market.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final JwtService jwtService;
    private final MemberRepository memberRepository;

    /**
     * OAuth2 로그인
     */
    public JwtTokenDto login(MemberLoginDto memberLoginDto) {
        // 회원 정보 조회
        Optional<Member> member = memberRepository.findByOauthIdAndRoles(memberLoginDto.getOauthId(), memberLoginDto.getRole());

        // 회원 정보가 없을 경우 회원가입 처리
        if (member.isEmpty()) {
            member = Optional.of(registerMember(memberLoginDto));
        }

        MemberJwtDto memberJwtDto = new MemberJwtDto(member.get());

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
                .roles(memberLoginDto.getRole())
                .build();

        return memberRepository.save(newMember);
    }

    /**
     * Access Token 및 Refresh Token 발급
     */
    private JwtTokenDto createAccessTokenAndRefreshToken(MemberJwtDto memberJwtDto) {
        String accessToken = jwtService.createAccessToken(memberJwtDto);
        String refreshToken = jwtService.createRefreshToken(memberJwtDto);

        return JwtTokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
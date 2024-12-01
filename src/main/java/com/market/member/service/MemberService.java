package com.market.member.service;

import com.market.core.exception.MemberException;
import com.market.member.dto.request.MemberUpdateRequest;
import com.market.member.dto.response.ProfileReadResponseDto;
import com.market.member.entity.Member;
import com.market.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.market.core.code.error.MemberErrorCode.NOT_FOUND_MEMBER_ID;

/**
 * 회원 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public void saveDeviceToken(Long memberId, String deviceToken) {

        Member member = memberRepository.findById(memberId).
                orElseThrow(() -> new MemberException(NOT_FOUND_MEMBER_ID));

        member.saveDeviceToken(deviceToken);
    }

    @Transactional
    public void deleteDeviceToken(Long memberId) {

        Member member = memberRepository.findById(memberId).
                orElseThrow(() -> new MemberException(NOT_FOUND_MEMBER_ID));

        member.deleteDeviceToken();
    }

    @Transactional
    public void updateMember(Long memberId, MemberUpdateRequest memberUpdateRequest) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(NOT_FOUND_MEMBER_ID));

        member.updateMember(memberUpdateRequest);
    }

    @Transactional(readOnly = true)
    public ProfileReadResponseDto readMemberProfile(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberException(NOT_FOUND_MEMBER_ID));

        return ProfileReadResponseDto.builder()
                .id(member.getId())
                .name(member.getName())
                .provider(member.getProvider().name())
                .build();
    }
}

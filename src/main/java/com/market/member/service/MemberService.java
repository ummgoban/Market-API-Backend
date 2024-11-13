package com.market.member.service;


import com.market.core.code.error.MemberErrorCode;
import com.market.core.exception.MemberException;
import com.market.member.entity.Member;
import com.market.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER_ID));

        member.saveDeviceToken(deviceToken);
    }
}

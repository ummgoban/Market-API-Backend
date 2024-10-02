package com.market.market.service;

import com.market.core.code.error.MarketErrorCode;
import com.market.core.code.error.MemberErrorCode;
import com.market.core.exception.MemberException;
import com.market.market.dto.request.MarketHoursRequest;
import com.market.market.entity.Market;
import com.market.market.repository.MarketRepository;
import com.market.member.entity.Member;
import com.market.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 가게 Update 관련 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class MarketUpdateService {

    private final MemberRepository memberRepository;
    private final MarketRepository marketRepository;

    /**
     * 영업 시간 및 픽업 시간을 설정합니다.
     */
    @Transactional
    public void setBusinessAndPickupHours(Long memberId, Long marketId, MarketHoursRequest marketHoursRequest) {
        // 회원 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER_ID));

        // 가게 조회
        Market market = marketRepository.findById(marketId)
                .orElseThrow(() -> new MemberException(MarketErrorCode.NOT_FOUND_MARKET_ID));

        // 영업 시간 설정
        market.setBusinessHours(marketHoursRequest.getOpenAt(), marketHoursRequest.getCloseAt());

        // 픽업 시간 설정
        market.setPickUpHours(marketHoursRequest.getPickupStartAt(), marketHoursRequest.getPickupEndAt());
    }
}
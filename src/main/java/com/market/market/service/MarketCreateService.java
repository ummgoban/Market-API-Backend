package com.market.market.service;

import com.market.core.code.error.MarketErrorCode;
import com.market.core.code.error.MemberErrorCode;
import com.market.core.exception.MarketException;
import com.market.core.exception.MemberException;
import com.market.market.dto.request.MarketRegisterRequest;
import com.market.market.dto.response.RegisterMarketResponse;
import com.market.market.entity.Market;
import com.market.market.repository.MarketRepository;
import com.market.member.entity.Member;
import com.market.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 가게 Create 관련 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class MarketCreateService {

    private final MemberRepository memberRepository;
    private final MarketRepository marketRepository;

    /**
     * 가게 등록
     */
    @Transactional
    public RegisterMarketResponse registerMarket(Long memberId, MarketRegisterRequest marketRegisterRequest) {
        // 회원 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER_ID));

        // 사업자 등록 번호 중복 체크
        if (marketRepository.existsByBusinessNumber(marketRegisterRequest.getBusinessNumber())) {
            throw new MarketException(MarketErrorCode.DUPLICATE_BUSINESS_NUMBER);
        }

        Market market = Market.builder()
                .member(member)
                .marketName(marketRegisterRequest.getMarketName())
                .businessNumber(marketRegisterRequest.getBusinessNumber())
                .address(marketRegisterRequest.getAddress())
                .specificAddress(marketRegisterRequest.getSpecificAddress())
                .contactNumber(marketRegisterRequest.getContactNumber())
                .build();

        return RegisterMarketResponse.builder()
                .marketId(marketRepository.save(market).getId())
                .build();
    }
}
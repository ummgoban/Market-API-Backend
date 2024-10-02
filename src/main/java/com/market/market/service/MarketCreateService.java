package com.market.market.service;

import com.market.core.code.error.MarketErrorCode;
import com.market.core.code.error.MemberErrorCode;
import com.market.core.exception.MarketException;
import com.market.core.exception.MemberException;
import com.market.market.dto.request.MarketRegisterRequest;
import com.market.market.dto.response.RegisterMarketResponse;
import com.market.market.dto.server.BusinessStatusResponseDto;
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
    private final BusinessStatusService businessStatusService;

    /**
     * 가게 등록
     */
    @Transactional
    public RegisterMarketResponse registerMarket(Long memberId, MarketRegisterRequest marketRegisterRequest) {
        // 회원 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER_ID));

        // 가게 이름 중복 체크
        if (marketRepository.existsByMarketName(marketRegisterRequest.getMarketName())) {
            throw new MarketException(MarketErrorCode.DUPLICATE_MARKET_NAME);
        }

        // 사업자 등록 번호 유효성 검증
        BusinessStatusResponseDto businessStatusResponseDto = businessStatusService.getBusinessStatus(marketRegisterRequest.getBusinessNumber());
        String taxType = businessStatusResponseDto.getData().get(0).getTaxType();
        String businessStatus = businessStatusResponseDto.getData().get(0).getBusinessStatus();
        if (!isValidBusinessNumber(taxType, businessStatus)) {
            throw new MarketException(MarketErrorCode.INVALID_BUSINESS_NUMBER);
        }

        Market market = Market.builder()
                .member(member)
                .marketName(marketRegisterRequest.getMarketName())
                .businessNumber(businessStatusResponseDto.getData().get(0).getBusinessNumber())
                .address(marketRegisterRequest.getAddress())
                .specificAddress(marketRegisterRequest.getSpecificAddress())
                .contactNumber(marketRegisterRequest.getContactNumber())
                .build();

        return RegisterMarketResponse.builder()
                .marketId(marketRepository.save(market).getId())
                .build();
    }

    /**
     * 세금 유형을 기반으로 사업자 등록 번호가 유효한지 여부를 확인
     */
    private boolean isValidBusinessNumber(String taxType, String businessStatus) {

        return !"국세청에 등록되지 않은 사업자등록번호입니다.".equals(taxType) &&
                !"휴업자".equals(businessStatus) &&
                !"폐업자".equals(businessStatus);
    }
}
package com.market.market.service;

import com.market.core.code.error.MarketErrorCode;
import com.market.core.code.error.MemberErrorCode;
import com.market.core.exception.MarketException;
import com.market.core.exception.MemberException;
import com.market.market.dto.request.MarketRegisterRequest;
import com.market.market.dto.response.RegisterMarketResponse;
import com.market.market.dto.server.NcpGeocodingInfoDto;
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
    private final GeocodingService geocodingService;

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

        // 주소 값을 이용해, 위도/경도 받아오기
        NcpGeocodingInfoDto latitudeAndLongitude = geocodingService.getLatitudeAndLongitude(marketRegisterRequest.getAddress());

        Market market = Market.builder()
                .member(member)
                .marketName(marketRegisterRequest.getName())
                .businessNumber(marketRegisterRequest.getBusinessNumber())
                .address(marketRegisterRequest.getAddress())
                .specificAddress(marketRegisterRequest.getSpecificAddress())
                .contactNumber(marketRegisterRequest.getContactNumber())
                .latitude(Double.parseDouble(latitudeAndLongitude.getAddresses().get(0).getY()))
                .longitude(Double.parseDouble(latitudeAndLongitude.getAddresses().get(0).getX()))
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

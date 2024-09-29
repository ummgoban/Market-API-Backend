package com.market.market.service;

import com.market.core.code.error.MarketErrorCode;
import com.market.core.code.error.MemberErrorCode;
import com.market.core.exception.MarketException;
import com.market.core.exception.MemberException;
import com.market.market.dto.response.*;
import com.market.market.dto.server.BusinessStatusResponseDto;
import com.market.market.dto.server.MarketPagingInfoDto;
import com.market.market.entity.Market;
import com.market.market.entity.MarketImage;
import com.market.market.repository.MarketImageRepository;
import com.market.market.repository.MarketRepository;
import com.market.member.entity.Member;
import com.market.member.repository.MemberRepository;
import com.market.market.repository.TagRepository;
import com.market.product.dto.response.ProductResponse;
import com.market.product.entity.Product;
import com.market.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 가게 Read 관련 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class MarketReadService {

    private final MemberRepository memberRepository;
    private final MarketRepository marketRepository;
    private final MarketImageRepository marketImageRepository;
    private final ProductRepository productRepository;
    private final TagRepository tagRepository;
    private final BusinessStatusService businessStatusService;

    /**
     * 가게 상세 조회 트랜잭션입니다.
     */
    @Transactional(readOnly = true)
    public MarketSpecificResponse findSpecificMarket(Long marketId) {
        // dev 머지 후, exception handler 구현
        Market market = marketRepository.findById(marketId).orElseThrow(() -> new MarketException(MarketErrorCode.NOT_FOUND_MARKET_ID));

        // 가게 이미지들 조회
        List<MarketImage> marketImages = marketImageRepository.findAllByMarketId(marketId);

        // 가게 상품들 조회
        List<Product> products = productRepository.findAllByMarketId(marketId);

        // 가게의 상품 정보를 담을 배열 선언
        List<ProductResponse> productResponses = new ArrayList<>();

        // 상품 각각에 대해 태그 정보 조회 후, productResponse 객체 생성 후 productResponses에 저장
        for (Product product : products) {
            List<String> tagNames = tagRepository.findAllByProductId(product.getId());

            ProductResponse productResponse = ProductResponse.from(product, tagNames);
            productResponses.add(productResponse);
        }

        return MarketSpecificResponse.from(market, marketImages, productResponses);
    }

    /**
     * 사용자의 가게 목록을 조회합니다.
     */
    public List<MarketListResponse> getMarketList(Long memberId) {
        // 회원 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER_ID));

        // 마켓 리스트 조회
        List<Market> marketList = marketRepository.findAllByMemberId(member.getId());

        return marketList.stream()
                .map(market -> MarketListResponse.builder()
                        .marketId(market.getId())
                        .marketName(market.getMarketName())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * 커서 기반 페이지네이션을 사용하여 전체 가게 목록을 조회합니다.
     */
    @Transactional(readOnly = true)
    public MarketPagingResponse findMarketByCursorId(Long cursorId, Integer size) {

        List<MarketPagingInfoResponse> response = new ArrayList<>();

        // market 엔티티와 businessInfo 엔티티 조인 후, 데이터 조회
        Slice<MarketPagingInfoDto> marketList = marketRepository.findMarketByCursorId(cursorId, size);

        marketList.getContent().forEach(infoDto -> {

            // 가게에 대해 가게의 이미지 데이터 조회 로직
            List<MarketImage> marketImages = marketImageRepository.findAllByMarketId(infoDto.getId());
            List<String> images = marketImages.stream().map(MarketImage::getImageUrl).toList();

            MarketPagingInfoResponse marketPagingInfoResponse = MarketPagingInfoResponse.builder()
                    .id(infoDto.getId())
                    .marketName(infoDto.getMarketName())
                    .address(infoDto.getAddress())
                    .specificAddress(infoDto.getSpecificAddress())
                    .openAt(infoDto.getOpenAt())
                    .closeAt(infoDto.getCloseAt())
                    .pickupStartAt(infoDto.getPickupStartAt())
                    .pickupEndAt(infoDto.getPickupEndAt())
                    .images(images)
                    .build();

            response.add(marketPagingInfoResponse);
        });

        return MarketPagingResponse.
                builder().
                markets(response).
                hasNext(marketList.hasNext()).
                build();
    }

    /**
     * 사업자 등록 번호 유효성 검증
     */
    public BusinessNumberValidationResponse validateBusinessStatus(String businessNumber) {
        BusinessStatusResponseDto businessStatusResponseDto = businessStatusService.getBusinessStatus(businessNumber);
        String taxType = businessStatusResponseDto.getData().get(0).getTaxType();
        String businessStatus = businessStatusResponseDto.getData().get(0).getBusinessStatus();

        return BusinessNumberValidationResponse.builder()
                .validBusinessNumber(isValidBusinessNumber(taxType, businessStatus))
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
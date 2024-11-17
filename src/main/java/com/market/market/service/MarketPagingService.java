package com.market.market.service;


import com.market.market.dto.response.MarketPagingInfoResponse;
import com.market.market.dto.response.MarketPagingResponse;
import com.market.market.dto.server.MarketPagingInfoDto;
import com.market.market.repository.MarketRepository;
import com.market.product.dto.response.ProductResponse;
import com.market.product.entity.Product;
import com.market.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 가게 페이징 관련 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class MarketPagingService {


    private final MarketRepository marketRepository;
    private final ProductRepository productRepository;

    private static final Double DEFAULT_LATITUDE = 37.582831666666664;
    private static final Double DEFAULT_LONGITUDE = 127.06107333333334;

    /**
     * 커서 기반 페이지네이션을 사용하여 전체 가게 목록을 조회합니다.
     */
    @Transactional(readOnly = true)
    public MarketPagingResponse getMarketByCursorId(Long cursorId, Integer size, Double userLatitude, Double userLongitude) {

        List<MarketPagingInfoResponse> response = new ArrayList<>();

        if (userLatitude == null || userLongitude == null) {
            userLatitude = DEFAULT_LATITUDE;
            userLongitude = DEFAULT_LONGITUDE;
        }

        // market 엔티티와 businessInfo 엔티티 조인 후, 데이터 조회
        Slice<MarketPagingInfoDto> marketList = marketRepository.findMarketByCursorId(cursorId, size, userLatitude, userLongitude);


        marketList.getContent().forEach(infoDto -> {

            List<Product> marketProducts = productRepository.findAllByMarketId(infoDto.getId());
            List<ProductResponse> productResponses = marketProducts.stream().map(ProductResponse::from).toList();

            MarketPagingInfoResponse marketPagingInfoResponse = MarketPagingInfoResponse.builder()
                    .id(infoDto.getId())
                    .name(infoDto.getMarketName())
                    .address(infoDto.getAddress())
                    .specificAddress(infoDto.getSpecificAddress())
                    .latitude(infoDto.getLatitude())
                    .longitude(infoDto.getLongitude())
                    .openAt(infoDto.getOpenAt())
                    .closeAt(infoDto.getCloseAt())
                    .pickupStartAt(infoDto.getPickupStartAt())
                    .pickupEndAt(infoDto.getPickupEndAt())
                    .products(productResponses)
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
     * 커서 기반 페이지네이션을 사용하여 회원의 찜 가게 목록을 조회합니다.
     */
    @Transactional(readOnly = true)
    public MarketPagingResponse getMemberMarketByCursorId(Long memberId, Long cursorId, Integer size) {

        List<MarketPagingInfoResponse> response = new ArrayList<>();

        // market 엔티티와 businessInfo 엔티티 조인 후, 데이터 조회
        Slice<MarketPagingInfoDto> marketList = marketRepository.findMemberLikeMarketByCursorId(memberId, cursorId, size);

        marketList.getContent().forEach(infoDto -> {

            List<Product> marketProducts = productRepository.findAllByMarketId(infoDto.getId());
            List<ProductResponse> productResponses = marketProducts.stream().map(ProductResponse::from).toList();

            MarketPagingInfoResponse marketPagingInfoResponse = MarketPagingInfoResponse.builder()
                    .id(infoDto.getId())
                    .name(infoDto.getMarketName())
                    .address(infoDto.getAddress())
                    .specificAddress(infoDto.getSpecificAddress())
                    .openAt(infoDto.getOpenAt())
                    .closeAt(infoDto.getCloseAt())
                    .pickupStartAt(infoDto.getPickupStartAt())
                    .pickupEndAt(infoDto.getPickupEndAt())
                    .products(productResponses)
                    .build();

            response.add(marketPagingInfoResponse);
        });

        return MarketPagingResponse.
                builder().
                markets(response).
                hasNext(marketList.hasNext()).
                build();
    }

}

package com.market.market.service;

import com.market.market.dto.server.MarketPagingInfoDto;
import com.market.market.dto.response.MarketPagingResponse;
import com.market.market.dto.response.MarketPagingInfoResponse;
import com.market.market.entity.MarketImage;
import com.market.market.repository.MarketImageRepository;
import com.market.market.repository.MarketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 가게 커서 기반 페이지 네이션의 비즈니스 로직입니다.
 */
@Service
@RequiredArgsConstructor
public class MarketPagingService {

    private final MarketRepository marketRepository;

    private final MarketImageRepository marketImageRepository;

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
}

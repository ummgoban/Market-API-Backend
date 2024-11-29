package com.market.market.service;

import com.market.core.code.error.MarketErrorCode;
import com.market.core.exception.MemberException;
import com.market.market.dto.request.MarketUpdateRequest;
import com.market.market.entity.Market;
import com.market.market.entity.MarketImage;
import com.market.market.repository.MarketImageRepository;
import com.market.market.repository.MarketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 가게 Update 관련 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class MarketUpdateService {

    private final MarketRepository marketRepository;
    private final MarketImageRepository marketImageRepository;

    /**
     * 가게 정보를 업데이트합니다.
     */
    @Transactional
    public void updateMarket(Long marketId, MarketUpdateRequest marketUpdateRequest) {
        // 가게 조회
        Market market = marketRepository.findById(marketId)
                .orElseThrow(() -> new MemberException(MarketErrorCode.NOT_FOUND_MARKET_ID));

        // 한 줄 소개 업데이트
        market.updateSummary(marketUpdateRequest.getSummary());

        // 영업 시간 업데이트
        market.updateBusinessHours(marketUpdateRequest.getOpenAt(), marketUpdateRequest.getCloseAt());

        // 픽업 시간 업데이트
        market.updatePickUpHours(marketUpdateRequest.getPickupStartAt(), marketUpdateRequest.getPickupEndAt());

        // 가게 사진 업데이트
        updateMarketImages(market, marketUpdateRequest.getImageUrls());
    }

    /**
     * 가게 사진을 업데이트합니다.
     */
    private void updateMarketImages(Market market, List<String> imageUrls) {
        // 기존 사진 List 조회
        List<MarketImage> existingMarketImages = marketImageRepository.findAllByMarketId(market.getId());

        if (!existingMarketImages.isEmpty()) {

            // 기존 사진 URL List 조회
            List<String> existingMarketImageUrls = existingMarketImages.stream()
                    .map(MarketImage::getImageUrl)
                    .toList();
            // DB에 없는 사진 저장
            for (String imageUrl : imageUrls) {
                if (!existingMarketImageUrls.contains(imageUrl)) {
                    MarketImage updateImage = MarketImage.builder()
                            .market(market)
                            .imageUrl(imageUrl)
                            .build();
                    marketImageRepository.save(updateImage);
                }
            }
            // 새로 추가된 리스트에 없는 이미지는 삭제
            for (MarketImage existingMarketImage : existingMarketImages) {
                if (!imageUrls.contains(existingMarketImage.getImageUrl())) {
                    marketImageRepository.delete(existingMarketImage);
                }
            }

            // 기존에 저장된 가게 사진이 없는 경우,
        } else {
            for (String imageUrl : imageUrls) {
                MarketImage updateImage = MarketImage.builder()
                        .market(market)
                        .imageUrl(imageUrl)
                        .build();
                marketImageRepository.save(updateImage);
            }
        }
    }
}
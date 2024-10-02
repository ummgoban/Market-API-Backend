package com.market.market.service;

import com.market.core.code.error.MarketErrorCode;
import com.market.core.code.error.S3ErrorCode;
import com.market.core.exception.MemberException;
import com.market.core.exception.S3Exception;
import com.market.core.s3.service.S3ImageService;
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
    private final S3ImageService s3ImageService;

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
        // 기존 사진 조회
        List<MarketImage> existingMarketImages = marketImageRepository.findAllByMarketId(market.getId());

        // DB에서 기존 사진 삭제
        marketImageRepository.deleteAll(existingMarketImages);

        // DB에 새로운 사진 저장
        for (String imageUrl : imageUrls) {
            // 파일이 존재하는지 확인
            if (!s3ImageService.doesImageExist(imageUrl)) {
                throw new S3Exception(S3ErrorCode.IMAGE_NOT_FOUND_ERROR);
            }

            MarketImage updateImage = MarketImage.builder()
                    .market(market)
                    .imageUrl(imageUrl)
                    .build();

            marketImageRepository.save(updateImage);
        }
    }
}
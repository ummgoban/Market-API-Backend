package com.market.market.service;

import com.market.core.code.error.MarketErrorCode;
import com.market.core.exception.MarketException;
import com.market.core.exception.MemberException;
import com.market.core.s3.service.S3ImageService;
import com.market.market.dto.request.MarketUpdateRequest;
import com.market.market.entity.Market;
import com.market.market.entity.MarketImage;
import com.market.market.repository.MarketImageRepository;
import com.market.market.repository.MarketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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

        // 가게 영업 시간 및 픽업 시간 업데이트
        updateBusinessAndPickupHours(market, marketUpdateRequest);
    }

    /**
     * 가게 영업 시간 및 픽업 시간을 업데이트합니다.
     */
    private void updateBusinessAndPickupHours(Market market, MarketUpdateRequest marketUpdateRequest) {
        // 영업 시간 업데이트
        market.setBusinessHours(marketUpdateRequest.getOpenAt(), marketUpdateRequest.getCloseAt());

        // 픽업 시간 업데이트
        market.setPickUpHours(marketUpdateRequest.getPickupStartAt(), marketUpdateRequest.getPickupEndAt());
    }

    /**
     * 가게 사진을 업데이트합니다.
     */
    @Transactional
    public void updateMarketImages(Long marketId, List<MultipartFile> updatedImages) {
        // 가게 사진은 3개 이상 필수
        if (updatedImages == null || updatedImages.size() < 3) {
            throw new MarketException(MarketErrorCode.IMAGE_UPLOAD_MINIMUM_ERROR);
        }

        // 가게 조회
        Market market = marketRepository.findById(marketId)
                .orElseThrow(() -> new MemberException(MarketErrorCode.NOT_FOUND_MARKET_ID));

        // 기존 사진 조회
        List<MarketImage> marketImageList = marketImageRepository.findAllByMarketId(market.getId());

        // S3에서 기존 사진 삭제
        for (MarketImage marketImage : marketImageList) {
            s3ImageService.deleteImage(marketImage.getImageUrl());
        }

        // DB에서 기존 사진 삭제
        marketImageRepository.deleteAll(marketImageList);

        // 새로운 사진 업로드 및 저장
        for (MultipartFile imageFile : updatedImages) {
            String imageUrl = s3ImageService.uploadImage(imageFile);

            MarketImage updateImage = MarketImage.builder()
                    .market(market)
                    .imageUrl(imageUrl)
                    .build();

            marketImageRepository.save(updateImage);
        }
    }
}
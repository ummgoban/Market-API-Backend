package com.market.market.service;

import com.market.core.s3.service.S3ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 가게 Delete 관련 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class MarketDeleteService {

    private final S3ImageService s3ImageService;

    /**
     * S3에사 가게 사진을 삭제합니다.
     */
    public void deleteMarketImage(String imageUrl) {
        s3ImageService.deleteImage(imageUrl);
    }
}
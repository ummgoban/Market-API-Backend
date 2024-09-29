package com.market.bucket.dto.response;


import com.market.bucket.dto.server.BucketMarketDto;
import com.market.bucket.dto.server.BucketProductDto;
import com.market.market.entity.Market;
import com.market.market.entity.MarketImage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * 장바구니 조회 응답 클래스입니다.
 */
@Getter
@Builder
@AllArgsConstructor
@Schema(description = "장바구니 조회 응답")
public class BucketProductResponse {

    @Schema(description = "가게 정보")
    private BucketMarketDto market;

    @Schema(description = "장바구니 상품 정보")
    private List<BucketProductDto> products;

    public static BucketProductResponse from(Market market, List<BucketProductDto> products, List<MarketImage> marketImage) {

        List<String> images = marketImage.stream().map(MarketImage::getImageUrl).toList();

        BucketMarketDto bucketMarketDto = BucketMarketDto.builder()
                .id(market.getId())
                .name(market.getMarketName())
                .images(images)
                .build();

        return BucketProductResponse.builder()
                .market(bucketMarketDto)
                .products(products)
                .build();
    }
}

package com.market.market.dto.response;

import com.market.market.entity.Market;
import com.market.market.entity.MarketImage;
import com.market.product.dto.response.ProductResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * 가게 상세 조회 응답 클래스입니다.
 */
@Getter
@Builder
@Schema(description = "가게 상세 조회 응답")
public class MarketSpecificResponse {

    @Schema(description = "가게 ID")
    private Long id;

    @Schema(description = "가게 이름")
    private String marketName;

    @Schema(description = "가게 한 줄 소개")
    private String summary;

    @Schema(description = "가게 사업자 번호")
    private String businessNumber;

    @Schema(description = "가게 주소")
    private String address;

    @Schema(description = "가게 상세 주소")
    private String specificAddress;

    @Schema(description = "가게 연락처")
    private String contactNumber;

    @Schema(description = "가게 오픈 시간")
    private String openAt;

    @Schema(description = "가게 마감 시간")
    private String closeAt;

    @Schema(description = "가게 픽업 시작 시간")
    private String pickupStartAt;

    @Schema(description = "가게 픽업 마감 시간")
    private String pickupEndAt;

    @Schema(description = "가게 이미지 목록")
    List<String> imageUrls;

    @Schema(description = "가게 상품 목록")
    List<ProductResponse> products;

    public static MarketSpecificResponse from(Market market, List<MarketImage> marketImages, List<ProductResponse> productResponses) {

        List<String> imageUrls = marketImages.stream().map(MarketImage::getImageUrl).toList();

        return MarketSpecificResponse.builder()
                .id(market.getId())
                .marketName(market.getMarketName())
                .summary(market.getSummary())
                .businessNumber(market.getBusinessNumber())
                .address(market.getAddress())
                .specificAddress(market.getSpecificAddress())
                .contactNumber(market.getContactNumber())
                .openAt(market.getOpenAt())
                .closeAt(market.getCloseAt())
                .pickupStartAt(market.getPickupStartAt())
                .pickupEndAt(market.getPickupEndAt())
                .imageUrls(imageUrls)
                .products(productResponses)
                .build();
    }
}

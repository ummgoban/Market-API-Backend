package com.market.market.dto.response;

import com.market.market.entity.BusinessInfo;
import com.market.market.entity.Market;
import com.market.market.entity.MarketImage;
import com.market.product.dto.response.ProductResponse;
import com.market.product.entity.Product;
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
    private String name;

    @Schema(description = "가게 사업자 번호")
    private String number;

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
    List<String> images;

    @Schema(description = "가게 상품 목록")
    List<ProductResponse> products;

    public static MarketSpecificResponse from(Market market, BusinessInfo businessInfo,
                                              List<MarketImage> marketImages, List<ProductResponse> productResponses) {

        List<String> images = marketImages.stream().map(MarketImage::getImageUrl).toList();

        return MarketSpecificResponse.builder()
                .id(market.getId())
                .name(businessInfo.getMarketName())
                .number(businessInfo.getNumber())
                .address(businessInfo.getAddress())
                .specificAddress(businessInfo.getSpecificAddress())
                .contactNumber(businessInfo.getContactNumber())
                .openAt(market.getOpenAt())
                .closeAt(market.getCloseAt())
                .pickupStartAt(market.getPickupStartAt())
                .pickupEndAt(market.getPickupEndAt())
                .images(images)
                .products(productResponses)
                .build();
    }
}

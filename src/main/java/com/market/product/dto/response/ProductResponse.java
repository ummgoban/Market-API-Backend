package com.market.product.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.market.market.dto.server.TagResponseDto;
import com.market.market.entity.Tag;
import com.market.product.entity.Product;
import com.market.product.entity.ProductStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * 상품 조회 응답 클래스입니다.
 */
@Getter
@Builder
@Schema(description = "상품 정보")
public class ProductResponse {

    @Schema(description = "상품 ID", example = "1")
    private Long id;

    @Schema(description = "상품 이미지", example = "https://.../ab123...456.png")
    private String image;

    @Schema(description = "상품 이름", example = "상품명")
    private String name;

    @Schema(description = "상품 원가", example = "10000")
    private Integer originPrice;

    @Schema(description = "상품 할인가", example = "8000")
    private Integer discountPrice;

    @Schema(description = "상품 할인율", example = "20")
    private Integer discountRate;

    @Schema(description = "상품 상태", example = "IN_STOCK")
    private ProductStatus productStatus;

    @Schema(description = "상품 재고", example = "2")
    private Integer stock;

    @Schema(description = "상품 태그")
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private List<TagResponseDto> tags;

    public static ProductResponse from(Product product, List<Tag> tags) {

        List<TagResponseDto> tagResponseDtos = tags.stream().map(TagResponseDto::from).toList();

        return ProductResponse.builder()
                .id(product.getId())
                .image(product.getProductImage())
                .name(product.getName())
                .originPrice(product.getOriginPrice())
                .discountPrice(product.getDiscountPrice())
                .discountRate(product.getDiscountRate())
                .productStatus(product.getProductStatus())
                .stock(product.getStock())
                .tags(tagResponseDtos)
                .build();
    }

    public static ProductResponse from(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .image(product.getProductImage())
                .name(product.getName())
                .originPrice(product.getOriginPrice())
                .discountPrice(product.getDiscountPrice())
                .discountRate(product.getDiscountRate())
                .stock(product.getStock())
                .build();
    }
}

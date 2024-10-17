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

    @Schema(description = "상품 ID")
    private Long id;

    @Schema(description = "상품 이미지")
    private String image;

    @Schema(description = "상품 이름")
    private String name;

    @Schema(description = "상품 원가")
    private Integer originPrice;

    @Schema(description = "상품 할인가")
    private Integer discountPrice;

    @Schema(description = "상품 할인율")
    private Integer discountRate;

    @Schema(description = "상품 상태")
    private ProductStatus productStatus;

    @Schema(description = "상품 재고")
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

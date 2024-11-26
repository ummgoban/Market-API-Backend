package com.market.orders.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.market.orders.dto.server.OrdersProductsDto;
import com.market.orders.entity.OrdersStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 회원의 주문 목록 조회 DTO 입니다.
 */
@Getter
@Builder
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Schema(description = "회원의 주문 목록 조회 DTO 입니다.")
public class MemberOrdersResponse {

    @Schema(description = "주문 ID")
    private String ordersId;

    @Schema(description = "가게 ID")
    private Long marketId;

    @Schema(description = "가게 이름")
    private String marketName;

    @Schema(description = "주문 접수된 시각")
    private LocalDateTime createdAt;

    @Schema(description = "픽업 완료 또는 주문 취소된 시각")
    private LocalDateTime doneAt;

    @Schema(description = "픽업 희망 시각")
    private LocalDateTime pickupReservedAt;

    @Schema(description = "주문 가격")
    private Integer ordersPrice;

    @Schema(description = "주문 상태")
    private OrdersStatus ordersStatus;

    @Schema(description = "요청사항")
    private String customerRequest;

    @Schema(description = "접수된 주문의 상품 정보들")
    private List<OrdersProductsDto> products;

    @Schema(description = "대표 이미지")
    private String imageUrl;
}

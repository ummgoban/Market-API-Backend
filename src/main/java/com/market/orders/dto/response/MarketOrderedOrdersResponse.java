package com.market.orders.dto.response;

import com.market.orders.dto.server.OrdersProductsDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 가게의 접수된 주문 목록 조회 DTO 입니다.
 */
@Getter
@Builder
@Schema(description = "가게의 접수된 주문 목록 조회 DTO 입니다.")
public class MarketOrderedOrdersResponse {

    @Schema(description = "주문 ID")
    private Long id;

    @Schema(description = "주문 접수된 시각")
    private LocalDateTime createdAt;

    @Schema(description = "픽업 희망 시각")
    private LocalDateTime pickupReservedAt;

    @Schema(description = "주문 가격")
    private Integer ordersPrice;

    @Schema(description = "주문자명")
    private String orderMemberName;

    @Schema(description = "접수된 주문의 상품 정보들")
    private List<OrdersProductsDto> products;
}
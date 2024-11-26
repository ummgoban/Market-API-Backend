package com.market.orders.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.market.orders.dto.server.OrdersProductsDto;
import com.market.orders.entity.Orders;
import com.market.orders.entity.OrdersStatus;
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
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Schema(description = "가게의 접수된 주문 목록 조회 DTO 입니다.")
public class OrdersResponse {

    @Schema(description = "주문 ID")
    private String id;

    @Schema(description = "주문 접수된 시각")
    private LocalDateTime createdAt;

    @Schema(description = "픽업 완료 또는 주문 취소된 시각")
    private LocalDateTime doneAt;

    @Schema(description = "픽업 희망 시각")
    private LocalDateTime pickupReservedAt;

    @Schema(description = "주문 가격")
    private Integer ordersPrice;

    @Schema(description = "주문자명")
    private String orderMemberName;

    @Schema(description = "주문 상태")
    private OrdersStatus ordersStatus;

    @Schema(description = "요청사항")
    private String customerRequest;

    @Schema(description = "접수된 주문의 상품 정보들")
    private List<OrdersProductsDto> products;

    public static OrdersResponse from(Orders orders, List<OrdersProductsDto> ordersProducts) {
        return OrdersResponse.builder()
                .id(orders.getId())
                .createdAt(orders.getCreatedAt())
                .pickupReservedAt(orders.getPickupReservedAt())
                .ordersPrice(orders.getOrdersPrice())
                .orderMemberName(orders.getMember().getName())
                .ordersStatus(orders.getOrdersStatus())
                .customerRequest(orders.getCustomerRequest())
                .products(ordersProducts)
                .build();
    }
}

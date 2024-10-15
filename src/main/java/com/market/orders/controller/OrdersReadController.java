package com.market.orders.controller;


import com.market.core.response.BfResponse;
import com.market.orders.annotation.ValidOrdersStatus;
import com.market.orders.dto.response.MarketOrdersResponse;
import com.market.orders.entity.OrdersStatus;
import com.market.orders.service.OrdersReadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 주문 Read 관련 API controller 입니다.
 */
@Validated
@RestController
@RequestMapping("order")
@RequiredArgsConstructor
@Tag(name = "주문 READ", description = "주문 READ 관련 API")
public class OrdersReadController {

    private final OrdersReadService ordersReadService;

    @Operation(
            summary = "가게의 주문 목록 조회",
            description = "가게의 주문 목록 조회입니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "가게의 주문 목록 조회 성공", useReturnTypeSchema = true)
    })
    @GetMapping("/market")
    public ResponseEntity<BfResponse<List<MarketOrdersResponse>>> getMarketOrders(
            @Parameter(description = "주문 상태 값. [접수 대기 : ORDERED, 주문 수락(픽업 대기) : ACCEPTED, 픽업완료/취소된 주문 : PICKUP_OR_CANCEL]")
            @RequestParam("ordersStatus") @ValidOrdersStatus String ordersStatus,
            @Parameter(description = "가게 ID")
            @RequestParam("marketId") Long marketId) {

        // 픽업완료/취소된 주문 조회
        if (ordersStatus.equals(OrdersStatus.PICKUP_OR_CANCEL.name())) {
            return ResponseEntity.ok(new BfResponse<>(ordersReadService.getMarketOrders(
                    new ArrayList<>(List.of(OrdersStatus.CANCEL, OrdersStatus.PICKUP)), marketId)));
        }

        // 접수 대기 or 픽업 대기 주문 조회
        OrdersStatus orderStatus = Arrays.stream(OrdersStatus.values()).
                filter(status -> status.name().equals(ordersStatus))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid order status: " + ordersStatus));

        return ResponseEntity.ok(new BfResponse<>(ordersReadService.getMarketOrders(
                new ArrayList<>(List.of(orderStatus)), marketId)));
    }

    @Operation(
            summary = "주문 상세 조회",
            description = "주문 상세 조회입니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주문 상세 목록 조회 성공", useReturnTypeSchema = true)
    })
    @GetMapping("/{orderId}")
    public ResponseEntity<BfResponse<MarketOrdersResponse>> getMarketOrders(
            @Parameter(description = "주문번호") @PathVariable("orderId") Long orderId) {
        return ResponseEntity.ok(new BfResponse<>(ordersReadService.getOrder(orderId)));
    }
}

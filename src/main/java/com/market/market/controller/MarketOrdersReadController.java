package com.market.market.controller;


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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 가게의 주문 Read 관련 API controller 입니다.
 */
@Validated
@RestController
@RequestMapping("markets")
@RequiredArgsConstructor
@Tag(name = "가게의 주문 READ", description = "가게의 주문 READ 관련 API")
public class MarketOrdersReadController {

    private final OrdersReadService ordersReadService;

    @Operation(
            summary = "가게의 주문 목록 조회",
            description = "가게의 주문 목록 조회입니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "가게의 주문 목록 조회 성공", useReturnTypeSchema = true)
    })
    @GetMapping("/orders")
    public ResponseEntity<BfResponse<List<MarketOrdersResponse>>> getMarketOrders(
            @Parameter(description = "주문 상태 값. [접수 대기 : ORDERED, 주문 수락(픽업 대기) : ACCEPTED, 픽업완료/취소된 주문 : PICKUPED_OR_CANCELED]")
            @RequestParam("ordersStatus") @ValidOrdersStatus String ordersStatus,
            @Parameter(description = "가게 ID")
            @RequestParam("marketId") Long marketId) {

        // 픽업완료/취소된 주문 조회
        if (ordersStatus.equals(OrdersStatus.PICKUPED_OR_CANCELED.name())) {
            return ResponseEntity.ok(new BfResponse<>(ordersReadService.getMarketOrders(
                    new ArrayList<>(List.of(OrdersStatus.CANCELED, OrdersStatus.PICKUPED)), marketId)));
        }

        // 접수 대기 or 픽업 대기 주문 조회
        OrdersStatus orderStatus = Arrays.stream(OrdersStatus.values()).
                filter(status -> status.name().equals(ordersStatus))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid order status: " + ordersStatus));

        return ResponseEntity.ok(new BfResponse<>(ordersReadService.getMarketOrders(
                new ArrayList<>(List.of(orderStatus)), marketId)));
    }


}

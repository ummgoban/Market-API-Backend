package com.market.orders.controller;


import com.market.core.response.BfResponse;
import com.market.orders.dto.response.MarketOrderedOrdersResponse;
import com.market.orders.service.OrdersReadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 주문 Read 관련 API controller 입니다.
 */
@RestController
@RequestMapping("order")
@RequiredArgsConstructor
@Tag(name = "주문 READ", description = "주문 READ 관련 API")
public class OrdersReadController {

    private final OrdersReadService ordersReadService;

    @Operation(
            summary = "가게의 접수된 주문 목록 조회",
            description = "가게의 접수된 주문 목록 조회입니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "가게의 접수된 주문 목록 조회 성공")
    })
    @GetMapping("/market/{marketId}")
    public ResponseEntity<BfResponse<List<MarketOrderedOrdersResponse>>> getMarketOrderedOrders(
            @Parameter(description = "가게 ID 입니다.") @PathVariable("marketId") Long marketId) {
        return ResponseEntity.ok(new BfResponse<>(ordersReadService.getMarketOrderedOrders(marketId)));
    }
}

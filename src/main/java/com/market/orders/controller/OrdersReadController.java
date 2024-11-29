package com.market.orders.controller;


import com.market.core.response.BfResponse;
import com.market.orders.dto.response.OrdersResponse;
import com.market.orders.service.OrdersReadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



/**
 * 주문 Read 관련 API controller 입니다.
 */
@RestController
@RequestMapping("orders")
@RequiredArgsConstructor
@Tag(name = "주문 READ", description = "주문 READ 관련 API")
public class OrdersReadController {

    private final OrdersReadService ordersReadService;

    @Operation(
            summary = "주문 상세 조회",
            description = "주문 상세 조회입니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주문 상세 목록 조회 성공", useReturnTypeSchema = true)
    })
    @GetMapping("/{orderId}")
    public ResponseEntity<BfResponse<OrdersResponse>> getMarketOrders(
            @Parameter(description = "주문번호") @PathVariable("orderId") String orderId) {
        return ResponseEntity.ok(new BfResponse<>(ordersReadService.getOrder(orderId)));
    }
}

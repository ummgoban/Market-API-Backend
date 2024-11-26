package com.market.orders.controller;


import com.market.core.response.BfResponse;
import com.market.core.security.principal.PrincipalDetails;
import com.market.orders.dto.request.OrdersCreateRequestDto;
import com.market.orders.dto.response.OrdersCreateResponseDto;
import com.market.orders.service.OrdersCreateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 주문 Create 관련 API controller 입니다.
 */
@RestController
@RequestMapping("orders")
@RequiredArgsConstructor
@Tag(name = "주문 CREATE", description = "주문 CREATE 관련 API")
public class OrdersCreateController {

    private final OrdersCreateService ordersCreateService;

    @Operation(
            summary = "주문 생성",
            description = "주문 생성입니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "주문 생성 성공", useReturnTypeSchema = true)
    })
    @PostMapping
    public ResponseEntity<BfResponse<OrdersCreateResponseDto>> createOrders(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @Valid @RequestBody OrdersCreateRequestDto ordersCreateRequestDto) {
        return ResponseEntity.ok(new BfResponse<>(ordersCreateService.createOrders(Long.parseLong(principalDetails.getUsername()), ordersCreateRequestDto)));
    }
}

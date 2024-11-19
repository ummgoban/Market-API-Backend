package com.market.member.controller;

import com.market.core.response.BfResponse;
import com.market.core.security.principal.PrincipalDetails;
import com.market.orders.dto.response.OrdersResponse;
import com.market.orders.service.OrdersReadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 회원의 주문 Read 관련 API controller 입니다.
 */
@RestController
@RequestMapping("members")
@RequiredArgsConstructor
@Tag(name = "회원의 주문 READ", description = "회원의 주문 READ 관련 API")
public class MemberOrdersReadController {

    private final OrdersReadService ordersReadService;

    @Operation(
            summary = "회원의 진행 중인 주문 목록 조회",
            description = "회원의 진행 중인 주문 목록 조회입니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원의 진행 중인 주문 목록 조회 성공", useReturnTypeSchema = true)
    })
    @GetMapping("/orders/progress")
    public ResponseEntity<BfResponse<List<OrdersResponse>>> getMemberOrdersInProgress(
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ResponseEntity.ok(new BfResponse<>(
                ordersReadService.getMemberOrdersInProgress(Long.parseLong(principalDetails.getUsername()))));
    }

    @Operation(
            summary = "회원의 주문 목록 조회",
            description = "회원의 주문 목록 조회입니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원의 주문 목록 조회 성공", useReturnTypeSchema = true)
    })
    @GetMapping("/orders")
    public ResponseEntity<BfResponse<List<OrdersResponse>>> getMemberOrders(
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ResponseEntity.ok(new BfResponse<>(
                ordersReadService.getMemberOrders(Long.parseLong(principalDetails.getUsername()))));
    }
}

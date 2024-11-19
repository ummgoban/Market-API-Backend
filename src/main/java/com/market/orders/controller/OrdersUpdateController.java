package com.market.orders.controller;


import com.market.core.code.success.GlobalSuccessCode;
import com.market.core.response.BfResponse;
import com.market.orders.annotation.ValidOrdersStatus;
import com.market.orders.entity.OrdersStatus;
import com.market.orders.service.OrdersUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 주문 Update 관련 API controller 입니다.
 */
@RestController
@RequestMapping("orders")
@RequiredArgsConstructor
@Tag(name = "주문 UPDATE", description = "주문 UPDATE 관련 API")
public class OrdersUpdateController {

    private final OrdersUpdateService ordersUpdateService;

    @Operation(
            summary = "주문 상태 업데이트",
            description = "특정 주문의 상태를 업데이트합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주문 상태 업데이트 성공",
                    content = @Content(examples = @ExampleObject(value = "{ \"code\": 200, \"message\": \"정상 처리되었습니다.\" }")))
    })
    @PatchMapping()
    public ResponseEntity<BfResponse<GlobalSuccessCode>> updateOrders(
            @Parameter(description = "변경할 주문 상태 값 [ 수락: ACCEPTED, 픽업 완료: PICKEDUP, 거절: CANCELED ]")
            @RequestParam("ordersStatus") @ValidOrdersStatus String ordersStatus,
            @Parameter(description = "주문 ID") @RequestParam("ordersId") Long ordersId) {

        OrdersStatus status = OrdersStatus.from(ordersStatus);
        ordersUpdateService.updateOrdersStatus(ordersId, status);

        return ResponseEntity.ok(new BfResponse<>(GlobalSuccessCode.SUCCESS));
    }

}

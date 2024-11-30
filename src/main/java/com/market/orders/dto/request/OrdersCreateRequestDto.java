package com.market.orders.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import javax.annotation.Nullable;
import java.time.LocalDateTime;

/**
 * 주문 등록 요청을 위한 DTO 클래스입니다.
 */
@Getter
public class OrdersCreateRequestDto {

    @NotNull
    @Schema(description = "픽업 희망 시간", required = true)
    private LocalDateTime pickupReservedAt;

    @Nullable
    @Schema(description = "고객 요청 사항")
    private String customerRequest;
}

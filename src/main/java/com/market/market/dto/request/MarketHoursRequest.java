package com.market.market.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MarketHoursRequest {

    @NotBlank(message = "영업 시작 시간은 필수 항목입니다.")
    @Schema(description = "영업 시작 시간", example = "09:00", required = true)
    private final String openAt;

    @NotBlank(message = "영업 종료 시간은 필수 항목입니다.")
    @Schema(description = "영업 종료 시간", example = "18:00", required = true)
    private final String closeAt;

    @NotBlank(message = "픽업 시작 시간은 필수 항목입니다.")
    @Schema(description = "픽업 시작 시간", example = "10:00", required = true)
    private final String pickupStartAt;

    @NotBlank(message = "픽업 종료 시간은 필수 항목입니다.")
    @Schema(description = "픽업 종료 시간", example = "17:00", required = true)
    private final String pickupEndAt;
}
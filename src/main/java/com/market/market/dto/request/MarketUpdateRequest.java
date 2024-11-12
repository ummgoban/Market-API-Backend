package com.market.market.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class MarketUpdateRequest {

    @NotBlank(message = "한 줄 소개는 필수 항목입니다.")
    @Schema(description = "한 줄 소개", example = "가게 한 줄 소개", required = true)
    private final String summary;

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

    @Size(min = 1, message = "가게 사진은 최소 1개 이상이어야 합니다.")
    @Size(max = 5, message = "가게 사진은 최대 5개입니다.")
    @Schema(description = "가게 사진의 URL 리스트", example = "[\"https://.../ab123...456.png\", \"https://.../cd123...456.png\"]", required = true)
    private final List<String> imageUrls;
}
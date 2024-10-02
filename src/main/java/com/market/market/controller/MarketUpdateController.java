package com.market.market.controller;

import com.market.core.code.success.GlobalSuccessCode;
import com.market.core.response.BfResponse;
import com.market.core.security.principal.PrincipalDetails;
import com.market.market.dto.request.MarketHoursRequest;
import com.market.market.service.MarketUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 가게 Update 관련 API controller 입니다.
 */
@RestController
@RequestMapping("market")
@RequiredArgsConstructor
@Tag(name = "가게 UPDATE", description = "가게 UPDATE 관련 API")
public class MarketUpdateController {

    private final MarketUpdateService marketUpdateService;

    @Operation(
            summary = "가게 영업 시간 및 픽업 시간 설정",
            description = "특정 가게의 영업 시간 및 픽업 시간을 설정합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "가게 영업 시간 및 픽업 시간 설정 성공",
                    content = @Content(examples = @ExampleObject(value = "{ \"code\": 200, \"message\": \"정상 처리되었습니다.\" }")))
    })
    @PatchMapping("/{marketId}/hours")
    public ResponseEntity<BfResponse> setBusinessAndPickupHours(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable("marketId") Long marketId,
            @Valid @RequestBody MarketHoursRequest marketHoursRequest
    ) {
        marketUpdateService.setBusinessAndPickupHours(Long.parseLong(principalDetails.getUsername()), marketId, marketHoursRequest);
        return ResponseEntity.ok(new BfResponse<>(GlobalSuccessCode.SUCCESS));
    }
}
package com.market.market.controller;

import com.market.core.code.success.GlobalSuccessCode;
import com.market.core.response.BfResponse;
import com.market.market.dto.request.MarketUpdateRequest;
import com.market.market.service.MarketUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 가게 Update 관련 API controller 입니다.
 */
@RestController
@RequestMapping("markets")
@RequiredArgsConstructor
@Tag(name = "가게 UPDATE", description = "가게 UPDATE 관련 API")
public class MarketUpdateController {

    private final MarketUpdateService marketUpdateService;

    @Operation(
            summary = "가게 정보 업데이트",
            description = "특정 가게의 정보를 업데이트합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "가게 정보 업데이트 성공",
                    content = @Content(examples = @ExampleObject(value = "{ \"code\": 200, \"message\": \"정상 처리되었습니다.\" }")))
    })
    @PatchMapping(value = "/{marketId}")
    public ResponseEntity<BfResponse<GlobalSuccessCode>> updateMarket(
            @Parameter(description = "가게 ID 입니다.")
            @PathVariable("marketId") Long marketId,
            @RequestBody @Valid MarketUpdateRequest marketUpdateRequest
    ) {
        marketUpdateService.updateMarket(marketId, marketUpdateRequest);
        return ResponseEntity.ok(new BfResponse<>(GlobalSuccessCode.SUCCESS));
    }
}
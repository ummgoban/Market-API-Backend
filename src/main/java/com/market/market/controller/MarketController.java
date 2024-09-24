package com.market.market.controller;


import com.market.core.response.BfResponse;
import com.market.market.dto.response.MarketSpecificResponse;
import com.market.market.service.MarketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 가게 관련 API controller 입니다.
 */
@RestController
@RequestMapping("/market")
@RequiredArgsConstructor
@Tag(name = "가게 API", description = "가게 관련 API 입니다.")
public class MarketController {

    private final MarketService marketService;


    @Operation(summary = "가게 상세 조회", description = "가게 상세 조회입니다.")
    @SecurityRequirements(value = {}) // no security
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "가게 상세 조회 성공")
    })
    @GetMapping("/{marketId}")
    public ResponseEntity<BfResponse<MarketSpecificResponse>> findSpecificMarket(
            @Parameter(description = "가게 ID 입니다.") @PathVariable("marketId") Long marketId) {
        return ResponseEntity.ok(new BfResponse<>(marketService.findSpecificMarket(marketId)));
    }
}

package com.market.market.controller;

import com.market.core.response.BfResponse;
import com.market.core.security.principal.PrincipalDetails;
import com.market.market.dto.request.MarketRegisterRequest;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 매장 관련 API controller 입니다.
 */
@RestController
@RequestMapping("market")
@RequiredArgsConstructor
@Tag(name = "매장", description = "매장 관련 API")
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

    @PostMapping()
    @Operation(
            summary = "매장 등록",
            description = "새로운 매장을 등록합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "매장 등록 성공", useReturnTypeSchema = true),
    })
    public ResponseEntity<BfResponse<Long>> registerMarket(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody MarketRegisterRequest marketRegisterRequest) {
        Long marketId = marketService.registerMarket(principalDetails, marketRegisterRequest);
        System.out.println("principalDetails = " + principalDetails.getUsername());
        return ResponseEntity.ok(new BfResponse<>(marketId));
    }

    @GetMapping("/business/validate")
    @Operation(
            summary = "사업자 등록 번호 유효성 검증",
            description = "사업자 등록 번호가 유효한지 확인합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사업자 번호가 유효하면 true, 그렇지 않으면 false 반환", useReturnTypeSchema = true)
    })
    public ResponseEntity<BfResponse<Boolean>> getBusinessStatus(@RequestParam String businessNumber) {
        boolean isValidBusinessNumber = marketService.validateBusinessStatus(businessNumber);
        return ResponseEntity.ok(new BfResponse<>(isValidBusinessNumber));
    }
}
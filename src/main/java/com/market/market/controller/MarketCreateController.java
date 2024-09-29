package com.market.market.controller;

import com.market.core.response.BfResponse;
import com.market.core.security.principal.PrincipalDetails;
import com.market.market.dto.request.MarketRegisterRequest;
import com.market.market.dto.response.RegisterMarketResponse;
import com.market.market.service.MarketCreateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 가게 Create 관련 API controller 입니다.
 */
@RestController
@RequestMapping("market")
@RequiredArgsConstructor
@Tag(name = "가게 CREATE", description = "가게 CREATE 관련 API")
public class MarketCreateController {

    private final MarketCreateService marketCreateService;

    @Operation(
            summary = "가게 등록",
            description = "새로운 가게를 등록합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "가게 등록 성공", useReturnTypeSchema = true),
    })
    @PostMapping()
    public ResponseEntity<BfResponse<RegisterMarketResponse>> registerMarket(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody MarketRegisterRequest marketRegisterRequest) {
        RegisterMarketResponse registerMarketResponse = marketCreateService.registerMarket(Long.parseLong(principalDetails.getUsername()), marketRegisterRequest);
        return ResponseEntity.ok(new BfResponse<>(registerMarketResponse));
    }
}
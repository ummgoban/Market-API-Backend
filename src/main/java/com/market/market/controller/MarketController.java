package com.market.market.controller;

import com.market.core.code.success.GlobalSuccessCode;
import com.market.core.response.BfResponse;
import com.market.core.security.principal.PrincipalDetails;
import com.market.market.dto.request.MarketHoursRequest;
import com.market.market.dto.request.MarketRegisterRequest;
import com.market.market.dto.response.BusinessNumberValidationResponse;
import com.market.market.dto.response.MarketListResponse;
import com.market.market.dto.response.MarketSpecificResponse;
import com.market.market.dto.response.RegisterMarketResponse;
import com.market.market.service.MarketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 가게 관련 API controller 입니다.
 */
@RestController
@RequestMapping("market")
@RequiredArgsConstructor
@Tag(name = "가게", description = "가게 관련 API")
public class MarketController {

    private final MarketService marketService;

    @Operation(
            summary = "가게 상세 조회",
            description = "가게 상세 조회입니다."
    )
    @SecurityRequirements(value = {})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "가게 상세 조회 성공")
    })
    @GetMapping("/{marketId}")
    public ResponseEntity<BfResponse<MarketSpecificResponse>> findSpecificMarket(
            @Parameter(description = "가게 ID 입니다.") @PathVariable("marketId") Long marketId) {
        return ResponseEntity.ok(new BfResponse<>(marketService.findSpecificMarket(marketId)));
    }

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
        RegisterMarketResponse registerMarketResponse = marketService.registerMarket(principalDetails, marketRegisterRequest);
        return ResponseEntity.ok(new BfResponse<>(registerMarketResponse));
    }


    @Operation(
            summary = "사업자 등록 번호 유효성 검증",
            description = "사업자 등록 번호가 유효한지 확인합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사업자 번호가 유효하면 true, 그렇지 않으면 false 반환", useReturnTypeSchema = true)
    })
    @GetMapping("/business/validate")
    public ResponseEntity<BfResponse<BusinessNumberValidationResponse>> getBusinessStatus(@RequestParam String businessNumber) {
        BusinessNumberValidationResponse businessNumberValidationResponse = marketService.validateBusinessStatus(businessNumber);
        return ResponseEntity.ok(new BfResponse<>(businessNumberValidationResponse));
    }
  
    @Operation(
            summary = "가게 목록 조회",
            description = "사용자의 가게 목록을 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "가게 목록이 없을 경우 data 필드가 생략", useReturnTypeSchema = true)
    })
    @GetMapping()
    public ResponseEntity<BfResponse<List<MarketListResponse>>> getMarketList(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        List<MarketListResponse> marketListResponses = marketService.getMarketList(principalDetails);
        return ResponseEntity.ok(new BfResponse<>(marketListResponses));
    }

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
        marketService.setBusinessAndPickupHours(principalDetails, marketId, marketHoursRequest);
        return ResponseEntity.ok(new BfResponse<>(GlobalSuccessCode.SUCCESS));
    }
}
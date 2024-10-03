package com.market.market.controller;

import com.market.core.response.BfResponse;
import com.market.core.security.principal.PrincipalDetails;
import com.market.market.dto.response.*;
import com.market.market.service.MarketReadService;
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

import java.util.List;

/**
 * 가게 Read 관련 API controller 입니다.
 */
@RestController
@RequestMapping("market")
@RequiredArgsConstructor
@Tag(name = "가게 READ", description = "가게 READ 관련 API")
public class MarketReadController {

    private final MarketReadService marketReadService;

    @Operation(
            summary = "가게 상세 조회",
            description = "가게 상세 조회입니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "가게 상세 조회 성공")
    })
    @GetMapping("/{marketId}")
    public ResponseEntity<BfResponse<MarketSpecificResponse>> findSpecificMarket(
            @Parameter(description = "가게 ID 입니다.") @PathVariable("marketId") Long marketId) {
        return ResponseEntity.ok(new BfResponse<>(marketReadService.findSpecificMarket(marketId)));
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
        List<MarketListResponse> marketListResponses = marketReadService.getMarketList(Long.parseLong(principalDetails.getUsername()));
        return ResponseEntity.ok(new BfResponse<>(marketListResponses));
    }

    @Operation(
            summary = "전체 가게 목록 페이징 조회",
            description = "커서 기반 페이지네이션을 사용하여 전체 가게 목록을 조회합니다."
    )
    @SecurityRequirements(value = {})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "가게 목록 조회 성공")
    })
    @GetMapping("/paging")
    public ResponseEntity<BfResponse<MarketPagingResponse>> findMarketByCursorId(
            @Parameter(description = "마지막으로 조회한 커서 ID 입니다. 가게 ID 입니다.") @RequestParam("cursorId") Long cursorId,
            @Parameter(description = "페이지의 크기 입니다.") @RequestParam("size") Integer size) {
        return ResponseEntity.ok(
                new BfResponse<>(marketReadService.findMarketByCursorId(cursorId, size)));
    }

    @Operation(
            summary = "사업자 등록 번호 유효성 검증",
            description = "사업자 등록 번호가 유효한지 확인합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사업자 번호가 유효하면 true, 그렇지 않으면 false 반환", useReturnTypeSchema = true)
    })
    @GetMapping("/verification/business-number")
    public ResponseEntity<BfResponse<BusinessNumberValidationResponse>> getBusinessStatus(
            @Parameter(description = "사업자 등록 번호입니다.") @RequestParam String businessNumber,
            @Parameter(description = "개업일자입니다.") @RequestParam String startDate,
            @Parameter(description = "이름입니다.") @RequestParam String name) {
        BusinessNumberValidationResponse businessNumberValidationResponse = marketReadService.validateBusinessValidate(businessNumber, startDate, name);
        return ResponseEntity.ok(new BfResponse<>(businessNumberValidationResponse));
    }
}
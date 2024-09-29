package com.market.market.controller;

import com.market.core.response.BfResponse;
import com.market.market.dto.response.MarketPagingResponse;
import com.market.market.service.MarketPagingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 가게 커서 기반 페이지네이션 API의 controller 입니다.
 */
@RestController
@RequestMapping("/market")
@RequiredArgsConstructor
@Tag(name = "가게 목록 API", description = "가게 목록 조회 관련 API 입니다.")
public class MarketPagingController {

    private final MarketPagingService marketPagingService;

    @Operation(
            summary = "가게 목록",
            description = "커서 기반 페이지네이션으로 가게 목록을 조회합니다."
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
                new BfResponse<>(marketPagingService.findMarketByCursorId(cursorId, size)));
    }
}

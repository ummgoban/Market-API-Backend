package com.market.member.controller;


import com.market.core.response.BfResponse;
import com.market.core.security.principal.PrincipalDetails;
import com.market.market.dto.response.MarketListResponse;
import com.market.market.dto.response.MarketPagingResponse;
import com.market.market.service.MarketPagingService;
import com.market.market.service.MarketReadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 회원의 가게 관련 API controller 입니다.
 */
@RestController
@RequestMapping("members")
@RequiredArgsConstructor
@Tag(name = "회원 가게", description = "회원의 가게 관련 API")
public class MemberMarketReadController {

    private final MarketPagingService marketPagingService;
    private final MarketReadService marketReadService;

    @Operation(
            summary = "회원의 가게 찜 목록 조회",
            description = "회원의 가게 찜 목록 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원의 가게 찜 목록 조회", useReturnTypeSchema = true)
    })
    @GetMapping("/markets/likes")
    public ResponseEntity<BfResponse<MarketPagingResponse>> findMemberMarketLikeList(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @Parameter(description = "마지막으로 조회한 커서 ID 입니다. 가게 ID 입니다.") @RequestParam("cursorId") Long cursorId,
            @Parameter(description = "페이지의 크기 입니다.") @RequestParam("size") Integer size) {

        return ResponseEntity.ok(new BfResponse<>(marketPagingService.getMemberMarketByCursorId(Long.parseLong(principalDetails.getUsername()), cursorId, size)));
    }

    @Operation(
            summary = "가게 목록 조회",
            description = "사용자의 가게 목록을 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "가게 목록이 없을 경우 data 필드가 생략", useReturnTypeSchema = true)
    })
    @GetMapping("/markets")
    public ResponseEntity<BfResponse<List<MarketListResponse>>> getMarketList(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        List<MarketListResponse> marketListResponses = marketReadService.getMarketList(Long.parseLong(principalDetails.getUsername()));
        return ResponseEntity.ok(new BfResponse<>(marketListResponses));
    }
}

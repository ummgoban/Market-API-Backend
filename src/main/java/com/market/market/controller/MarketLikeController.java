package com.market.market.controller;


import com.market.core.code.success.GlobalSuccessCode;
import com.market.core.response.BfResponse;
import com.market.core.security.principal.PrincipalDetails;
import com.market.market.dto.response.MarketPagingResponse;
import com.market.market.service.MarketLikeService;
import com.market.market.service.MarketPagingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 가게 찜 관련 API controller 입니다.
 */
@RestController
@RequestMapping("market")
@RequiredArgsConstructor
@Tag(name = "가게 찜", description = "가게 찜 관련 API")
public class MarketLikeController {

    private final MarketLikeService marketLikeService;
    private final MarketPagingService marketPagingService;

    @Operation(
            summary = "가게 찜",
            description = "가게를 찜합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "가게 찜 성공",
                    content = @Content(examples = @ExampleObject(value = "{ \"code\": 201, \"message\": \"정상적으로 생성되었습니다.\" }")))
    })
    @PostMapping("/{marketId}/like")
    public ResponseEntity<BfResponse<Void>> createMarketLike(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @Parameter(description = "가게 ID") @PathVariable("marketId") Long marketId) {

        marketLikeService.createMarketLike(Long.parseLong(principalDetails.getUsername()), marketId);

        return ResponseEntity.ok(new BfResponse<>(GlobalSuccessCode.CREATE));
    }


    @Operation(
            summary = "가게 찜 취소",
            description = "가게를 찜 취소합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "가게 찜 취소",
                    content = @Content(examples = @ExampleObject(value = "{ \"code\": 200, \"message\": \"정상적으로 처리되었습니다.\" }")))
    })
    @DeleteMapping("/{marketId}/like")
    public ResponseEntity<BfResponse<Void>> deleteMarketLike(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @Parameter(description = "가게 ID") @PathVariable("marketId") Long marketId) {

        marketLikeService.deleteMarketLike(Long.parseLong(principalDetails.getUsername()), marketId);

        return ResponseEntity.ok(new BfResponse<>(GlobalSuccessCode.CREATE));
    }

    @Operation(
            summary = "회원의 가게 찜 목록 조회",
            description = "회원의 가게 찜 목록 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원의 가게 찜 목록 조회", useReturnTypeSchema = true)
    })
    @GetMapping("/like")
    public ResponseEntity<BfResponse<MarketPagingResponse>> findMemberMarketLikeList(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @Parameter(description = "마지막으로 조회한 커서 ID 입니다. 가게 ID 입니다.") @RequestParam("cursorId") Long cursorId,
            @Parameter(description = "페이지의 크기 입니다.") @RequestParam("size") Integer size) {

        return ResponseEntity.ok(new BfResponse<>(marketPagingService.getMemberMarketByCursorId(Long.parseLong(principalDetails.getUsername()), cursorId, size)));
    }
}
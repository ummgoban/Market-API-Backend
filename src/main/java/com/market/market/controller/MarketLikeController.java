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
@RequestMapping("markets")
@RequiredArgsConstructor
@Tag(name = "가게 찜", description = "가게 찜 관련 API")
public class MarketLikeController {

    private final MarketLikeService marketLikeService;
    private final MarketPagingService marketPagingService;

    @Operation(
            summary = "가게 찜 상태 변경하기",
            description = "이미 찜했으면 찜을 취소한다. 기존에 찜하지 않았으면 찜 추가한다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "가게 찜 상태 변경 성공",
                    content = @Content(examples = @ExampleObject(value = "{ \"code\": 200, \"message\": \"정상적으로 처리되었습니다.\" }")))
    })
    @PostMapping("/{marketId}/likes")
    public ResponseEntity<BfResponse<Void>> createMarketLikeOrDeleteMarketLike(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @Parameter(description = "가게 ID") @PathVariable("marketId") Long marketId) {

        marketLikeService.createMarketLikeOrDeleteMarketLike(Long.parseLong(principalDetails.getUsername()), marketId);

        return ResponseEntity.ok(new BfResponse<>(GlobalSuccessCode.SUCCESS));
    }

}

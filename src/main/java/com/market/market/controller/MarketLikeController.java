package com.market.market.controller;


import com.market.core.code.success.GlobalSuccessCode;
import com.market.core.response.BfResponse;
import com.market.core.security.principal.PrincipalDetails;
import com.market.market.service.MarketLikeService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 가게 찜 관련 API controller 입니다.
 */
@RestController
@RequestMapping("market")
@RequiredArgsConstructor
@Tag(name = "가게 찜", description = "가게 찜 관련 API")
public class MarketLikeController {

    private final MarketLikeService marketLikeService;

    @PostMapping("/{marketId}/like")
    public ResponseEntity<BfResponse<Void>> createMarketLike(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @Parameter(description = "가게 ID") @PathVariable("marketId") Long marketId) {

        marketLikeService.createMarketLike(1L, marketId);

        return ResponseEntity.ok(new BfResponse<>(GlobalSuccessCode.CREATE));
    }
}

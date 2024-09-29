package com.market.bucket.controller;


import com.market.bucket.dto.request.BucketSaveRequest;
import com.market.bucket.dto.response.BucketDiscriminationResponse;
import com.market.bucket.dto.response.BucketProductResponse;
import com.market.bucket.service.BucketService;
import com.market.core.response.BfResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 장바구니 관련 API controller 입니다.
 */
@RestController
@RequestMapping("/bucket")
@RequiredArgsConstructor
@Tag(name = "장바구니", description = "장바구니 관련 API")
public class BucketController {

    private final BucketService bucketService;

    @Operation(summary = "장바구니 조회", description = "장바구니 조회입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "가게 상세 조회 성공")
    })
    @GetMapping
    public ResponseEntity<BfResponse<BucketProductResponse>> findBucket(@AuthenticationPrincipal String memberId) {
        return ResponseEntity.ok(new BfResponse<>(bucketService.findBucket(Long.parseLong(memberId))));
    }

    @Operation(summary = "장바구니 다른 가게 상품 여부 확인", description = "장바구니에 현재 가게와 다른 가게의 상품이 존재하는지 여부를 확인합니다..")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "가게 상품 여부 확인 성공")
    })
    @GetMapping("/{marketId}/check-product")
    public ResponseEntity<BfResponse<BucketDiscriminationResponse>> discriminateBucket(
            @AuthenticationPrincipal String memberId,
            @Parameter(description = "현재 장바구니에 담고자 하는 상품의 가게 ID 입니다.") @PathVariable("marketId") Long marketId) {
        return ResponseEntity.ok(new BfResponse<>(new BucketDiscriminationResponse(
                bucketService.discriminateBucket(marketId, Long.parseLong(memberId)))));
    }

    @Operation(summary = "장바구니 상품 추가", description = "장바구니 상품 추가 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "장바구니 상품 추가 성공")
    })
    @PostMapping("/{marketId}")
    public ResponseEntity<BfResponse<String>> saveBucket(
            @AuthenticationPrincipal String memberId,
            @RequestBody BucketSaveRequest bucketSaveRequest,
            @Parameter(description = "현재 장바구니에 담고자 하는 상품의 가게 ID 입니다.") @PathVariable("marketId") Long marketId) {
        bucketService.saveBucket(Long.parseLong("1"), marketId, bucketSaveRequest.getProducts());
        return ResponseEntity.ok(new BfResponse<>("상품 추가 성공"));
    }
}

package com.market.product.controller;

import com.market.core.code.success.GlobalSuccessCode;
import com.market.core.response.BfResponse;
import com.market.core.s3.dto.response.ImageUrlResponse;
import com.market.core.s3.service.ImageUrlService;
import com.market.product.dto.request.ProductCreateRequest;
import com.market.product.dto.request.ProductUpdateRequest;
import com.market.product.dto.response.ProductResponse;
import com.market.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 상품 관련 API controller 입니다.
 */
@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@Tag(name = "상품", description = "상품 관련 API")
public class ProductController {

    private final ImageUrlService imageUrlService;
    private final ProductService productService;

    @Operation(
            summary = "S3 Bucket에 상품 사진 업로드",
            description = "S3 Bucket에 상품의 사진을 업로드합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "S3 상품 사진 업로드 성공", useReturnTypeSchema = true),
    })
    @PostMapping(value = "/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BfResponse<ImageUrlResponse>> uploadMarketImage(
            @Parameter(description = "상품 사진입니다.")
            @RequestPart("updateImage") MultipartFile updateImage
    ) {
        ImageUrlResponse imageUrl = imageUrlService.uploadImage(updateImage);
        return ResponseEntity.ok(new BfResponse<>(imageUrl));
    }

    @Operation(
            summary = "상품 등록",
            description = "상품을 등록합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 등록 성공", useReturnTypeSchema = true),
    })
    @PostMapping()
    public ResponseEntity<BfResponse<GlobalSuccessCode>> createProduct(
            @RequestParam Long marketId,
            @RequestBody ProductCreateRequest productCreateRequest
    ) {
        productService.createProduct(marketId, productCreateRequest);
        return ResponseEntity.ok(new BfResponse<>(GlobalSuccessCode.SUCCESS));
    }

    @Operation(
            summary = "상품 목록 조회",
            description = "상품 목록을 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 목록 조회 성공", useReturnTypeSchema = true),
    })
    @GetMapping()
    public ResponseEntity<BfResponse<List<ProductResponse>>> createProduct(
            @RequestParam Long marketId
    ) {
        List<ProductResponse> productResponses = productService.getProducts(marketId);
        return ResponseEntity.ok(new BfResponse<>(productResponses));
    }

    @Operation(
            summary = "상품 수정",
            description = "상품을 수정합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 수정 성공", useReturnTypeSchema = true),
    })
    @PatchMapping("/{productId}")
    public ResponseEntity<BfResponse<GlobalSuccessCode>> updateProduct(
            @PathVariable Long productId,
            @RequestBody ProductUpdateRequest productUpdateRequest
    ) {
        productService.updateProduct(productId, productUpdateRequest);
        return ResponseEntity.ok(new BfResponse<>(GlobalSuccessCode.SUCCESS));
    }
}

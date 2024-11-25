package com.market.product.controller;

import com.market.core.code.success.GlobalSuccessCode;
import com.market.core.response.BfResponse;
import com.market.core.s3.dto.response.ImageUrlResponse;
import com.market.core.s3.service.ImageUrlService;
import com.market.core.security.principal.PrincipalDetails;
import com.market.product.dto.request.ProductCreateRequest;
import com.market.product.dto.request.ProductStockUpdateRequest;
import com.market.product.dto.request.ProductUpdateRequest;
import com.market.product.dto.response.ProductResponse;
import com.market.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 상품 관련 API controller 입니다.
 */
@RestController
@RequestMapping("/products")
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
    public ResponseEntity<BfResponse<ImageUrlResponse>> uploadProductImage(
            @Parameter(description = "상품 사진입니다.")
            @RequestPart("updateImage") MultipartFile updateImage) {
        ImageUrlResponse imageUrl = imageUrlService.uploadImage(updateImage);
        return ResponseEntity.ok(new BfResponse<>(imageUrl));
    }

    @Operation(
            summary = "S3 Bucket에서 상품 사진 삭제",
            description = "S3 Bucket에서 상품의 사진을 삭제합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "S3 상품 사진 삭제 성공",
                    content = @Content(examples = @ExampleObject(value = "{ \"code\": 200, \"message\": \"정상 처리되었습니다.\" }")))
    })
    @DeleteMapping("/images")
    public ResponseEntity<BfResponse<GlobalSuccessCode>> deleteProductImage(
            @Parameter(description = "사진 URL입니다.", example = "https://.../ecc84...203.png")
            @RequestParam("imageUrl") String imageUrl
    ) {
        imageUrlService.deleteImage(imageUrl);
        return ResponseEntity.ok(new BfResponse<>(GlobalSuccessCode.SUCCESS));
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
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @Parameter(description = "마켓 ID") @RequestParam("marketId") Long marketId,
            @RequestBody ProductCreateRequest productCreateRequest) {
        productService.createProduct(Long.parseLong(principalDetails.getUsername()), marketId, productCreateRequest);
        return ResponseEntity.ok(new BfResponse<>(GlobalSuccessCode.SUCCESS));
    }

    @Operation(
            summary = "상품 목록 조회",
            description = "상품 목록을 조회합니다."
    )
    @SecurityRequirements(value = {})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 목록 조회 성공", useReturnTypeSchema = true),
    })
    @GetMapping()
    public ResponseEntity<BfResponse<List<ProductResponse>>> createProduct(
            @Parameter(description = "마켓 ID") @RequestParam Long marketId) {
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
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody ProductUpdateRequest productUpdateRequest) {
        productService.updateProduct(Long.parseLong(principalDetails.getUsername()), productId, productUpdateRequest);
        return ResponseEntity.ok(new BfResponse<>(GlobalSuccessCode.SUCCESS));
    }


    @Operation(
            summary = "상품 재고 수정",
            description = "가게 사장님이 상품의 재고를 수정합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 재고 수정 성공", useReturnTypeSchema = true),
    })
    @PatchMapping()
    public ResponseEntity<BfResponse<GlobalSuccessCode>> updateProductStock(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody ProductStockUpdateRequest productStockUpdateRequest) {
        productService.updateProductStock(Long.parseLong(principalDetails.getUsername()), productStockUpdateRequest);
        return ResponseEntity.ok(new BfResponse<>(GlobalSuccessCode.SUCCESS));
    }

    @Operation(
            summary = "상품 삭제",
            description = "상품을 삭제합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 삭제 성공", useReturnTypeSchema = true),
    })
    @DeleteMapping("/{productId}")
    public ResponseEntity<BfResponse<GlobalSuccessCode>> updateProduct(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable Long productId) {
        productService.deleteProduct(Long.parseLong(principalDetails.getUsername()), productId);
        return ResponseEntity.ok(new BfResponse<>(GlobalSuccessCode.SUCCESS));
    }
}

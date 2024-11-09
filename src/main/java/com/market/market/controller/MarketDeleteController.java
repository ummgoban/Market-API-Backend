package com.market.market.controller;

import com.market.core.code.success.GlobalSuccessCode;
import com.market.core.response.BfResponse;
import com.market.core.s3.service.ImageUrlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 가게 Delete 관련 API controller 입니다.
 */
@RestController
@RequestMapping("markets")
@RequiredArgsConstructor
@Tag(name = "가게 DELETE", description = "가게 DELETE 관련 API")
public class MarketDeleteController {

    private final ImageUrlService imageUrlService;

    @Operation(
            summary = "S3 Bucket에서 가게 사진 삭제",
            description = "S3 Bucket에서 가게의 사진을 삭제합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "S3 가게 사진 삭제 성공",
                    content = @Content(examples = @ExampleObject(value = "{ \"code\": 200, \"message\": \"정상 처리되었습니다.\" }")))
    })
    @DeleteMapping( "/images")
    public ResponseEntity<BfResponse<GlobalSuccessCode>> deleteMarketImage(
            @Parameter(description = "사진 URL입니다.", example = "https://.../ecc84...203.png")
            @RequestParam("imageUrl") String imageUrl
    ) {
        imageUrlService.deleteImage(imageUrl);
        return ResponseEntity.ok(new BfResponse<>(GlobalSuccessCode.SUCCESS));
    }
}
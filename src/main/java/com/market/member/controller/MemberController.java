package com.market.member.controller;


import com.market.core.code.success.GlobalSuccessCode;
import com.market.core.response.BfResponse;
import com.market.core.security.principal.PrincipalDetails;
import com.market.member.dto.request.MemberUpdateRequest;
import com.market.member.service.MemberService;
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
 * 회원 관련 controller
 */
@RestController
@RequestMapping("members")
@RequiredArgsConstructor
@Tag(name = "회원", description = "회원 관련 API")
public class MemberController {

    private final MemberService memberService;

    @Operation(
            summary = "회원 기기 등록 토큰 저장",
            description = "회원 기기 등록 토큰 저장입니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 기기 등록 토큰 저장 성공")
    })
    @PostMapping("/device-token")
    public ResponseEntity<BfResponse<Void>> saveDeviceToken(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @Parameter(description = "기기 등록 토큰") @RequestParam("deviceToken") String deviceToken) {

        memberService.saveDeviceToken(Long.parseLong(principalDetails.getUsername()), deviceToken);

        return ResponseEntity.ok(new BfResponse<>(GlobalSuccessCode.SUCCESS));
    }

    @Operation(
            summary = "회원 정보 수정",
            description = "회원 정보를 수정합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 정보 수정 성공",
                    content = @Content(examples = @ExampleObject(value = "{ \"code\": 200, \"message\": \"정상 처리되었습니다.\" }")))

    })
    @PatchMapping("")
    public ResponseEntity<BfResponse<Void>> updateMember(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody MemberUpdateRequest memberUpdateRequest) {
        memberService.updateMember(Long.parseLong(principalDetails.getUsername()), memberUpdateRequest);
        return ResponseEntity.ok(new BfResponse<>(GlobalSuccessCode.SUCCESS));
    }
}

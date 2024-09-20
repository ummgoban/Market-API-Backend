package com.market.core.security.dto.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

/**
 * Naver 사용자 정보 DTO 클래스입니다.
 */
@Getter
public class NaverUserInfoDto {

    @JsonProperty("resultcode")
    @NotNull
    private String resultCode;

    @JsonProperty("message")
    @NotNull
    private String message;

    @JsonProperty("response")
    @NotNull
    private Response response;

    @Getter
    public static class Response {

        @JsonProperty("id")
        @NotNull
        private String oauthId;

        @JsonProperty("name")
        @NotNull
        private String name;

        @JsonProperty("profile_image")
        @NotNull
        private String profileImageUrl;
    }
}

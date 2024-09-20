package com.market.core.security.dto.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

/**
 * Kakao 사용자 정보 DTO 클래스입니다.
 */
@Getter
public class KakaoUserInfoDto {

    @JsonProperty("id")
    @NotNull
    private Long oauthId;

    @JsonProperty("kakao_account")
    @NotNull
    private KakaoAccount kakaoAccount;

    @Getter
    public static class KakaoAccount {

        @JsonProperty("profile")
        @NotNull
        private Profile profile;

        @Getter
        public static class Profile {

            @JsonProperty("nickname")
            @NotNull
            private String name;

            @JsonProperty("profile_image_url")
            @NotNull
            private String profileImageUrl;
        }
    }
}
package com.market.member.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;


/**
 * 회원 정보를 관리하는 엔티티 클래스입니다.
 */
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "oauth_id")
    private String oauthId; // 소셜 회원 고유 ID

    @Column(name = "provider")
    @Enumerated(EnumType.STRING)
    private ProviderType provider; // OAuth 제공자 정보

    @Column(name = "name")
    private String name; // 사용자 이름

    @Column(name = "profile_image_url")
    private String profileImageUrl; // 프로필 이미지 URL

    @Column(name = "roles")
    @Enumerated(EnumType.STRING)
    private RolesType roles; // 권한

    @Column(name = "device_token")
    private String deviceToken;

    @Column(name = "device_token_created_at")
    private LocalDateTime deviceTokenCreatedAt;

    public void saveDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
        this.deviceTokenCreatedAt = LocalDateTime.now();
    }

}

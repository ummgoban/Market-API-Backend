package com.market.member.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String oauthId; // 소셜 회원 고유 ID

    private String provider; // OAuth2 제공자 정보

    private String name; // 사용자 이름

    private String profileImageUrl; // 프로필 이미지 URL

    @Enumerated(EnumType.STRING)
    private RolesType roles; // 권한
}

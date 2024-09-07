package com.market.member.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String oauthId; // 소셜 회원 고유 ID

    private String name; // 이름

    private String profileImageUrl; // 프로필 이미지

    @Enumerated(EnumType.STRING)
    private RolesType roles = RolesType.ROLE_USER; // 권한
}

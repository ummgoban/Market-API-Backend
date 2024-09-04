package com.market.member.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // 이름

    private String email; // 이메일

    private String password; // 비밀번호

    @Enumerated(EnumType.STRING)
    private RolesType roles = RolesType.ROLE_USER; // 권한
}

package com.market.member.dto.request;

import com.market.member.entity.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Optional;

@Getter
public class MemberJwtDto {

    private final Long id;
    private final List<GrantedAuthority> authorities;

    public MemberJwtDto(Member member) {
        this.id = member.getId();
        this.authorities = Optional.ofNullable(member.getRoles())
                .map(role -> List.<GrantedAuthority>of(new SimpleGrantedAuthority(role.name())))
                .orElse(List.of());
    }
}
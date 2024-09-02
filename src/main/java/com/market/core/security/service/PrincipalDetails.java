package com.market.core.security.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.market.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Getter
@Builder
@AllArgsConstructor
public class PrincipalDetails implements UserDetails {

    private final long id;
    private final String email;
    @JsonIgnore
    private final String password;
    private final List<GrantedAuthority> authorities;

    // 생성자
    public PrincipalDetails(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.password = member.getPassword();
        this.authorities = Optional.ofNullable(member.getRoles())
                .map(role -> List.<GrantedAuthority>of(new SimpleGrantedAuthority(role.name())))
                .orElse(List.of());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities; // 권한 리스트 반환
    }

    @Override
    public String getPassword() {
        return password; // 사용자 비밀번호 반환
    }

    @Override
    public String getUsername() {
        return email; // 사용자 이메일 반환
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정 만료 여부
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정 잠김 여부
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 자격 증명 만료 여부
    }

    @Override
    public boolean isEnabled() {
        return true; // 계정 활성화 여부
    }
}

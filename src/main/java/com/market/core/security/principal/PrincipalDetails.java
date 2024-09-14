package com.market.core.security.principal;

import com.market.member.entity.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * PrincipalDetailsService에서 사용되며, 사용자의 인증 정보를 담고 있는 UserDetails 구현 클래스입니다.
 */
@Getter
@RequiredArgsConstructor
public class PrincipalDetails implements UserDetails {

    private final Long id;
    private final List<GrantedAuthority> authorities;

    public PrincipalDetails(Member member) {
        this.id = member.getId();
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
        return null; // 사용자 비밀번호 반환, 소셜 로그인이므로 null
    }

    @Override
    public String getUsername() {
        return String.valueOf(id); // 사용자 ID 반환
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

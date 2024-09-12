package com.market.member.repository;

import com.market.member.entity.Member;
import com.market.member.entity.RolesType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByOauthIdAndRoles(String oauthId, RolesType rolesType);
}

package com.market.member.repository;

import com.market.member.entity.Member;
import com.market.member.entity.RolesType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Member 엔티티에 대한 JPA 레포지토리입니다.
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByOauthIdAndRoles(String oauthId, RolesType rolesType);
}

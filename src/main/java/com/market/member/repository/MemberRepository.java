package com.market.member.repository;

import com.market.member.entity.Member;
import com.market.member.entity.RolesType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * 회원 도메인의 Repository 입니다.
 */
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByOauthIdAndRoles(String oauthId, RolesType rolesType);

    @Query("select me from Member me inner join Market ma on ma.member.id = me.id where ma.id = :marketId")
    Optional<Member> findMemberByMarketId(@Param("marketId") Long marketId);
}
package com.market.market.repository;

import com.market.market.entity.Market;
import com.market.market.entity.MarketLike;
import com.market.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MarketLikeRepository extends JpaRepository<MarketLike, Long> {

    @Query("select m from MarketLike m where m.member.id = :memberId and m.market.id = :marketId")
    Optional<MarketLike> findMarketLikeByMemberAndMarket(@Param("memberId") Long memberId,
                                                         @Param("marketId") Long marketId);

    boolean existsByMemberAndMarket(Member member, Market market);
}
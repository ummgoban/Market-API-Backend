package com.market.market.repository;

import com.market.market.entity.Market;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

import java.util.List;

/**
 * 가게 도메인의 Repository 입니다.
 */
public interface MarketRepository extends JpaRepository<Market, Long>, MarketPagingRepository {

    boolean existsByMarketName(String marketName);

    boolean existsByBusinessNumber(String businessNumber);

    List<Market> findAllByMemberId(Long memberId);
  
    @Query("select m from Product p inner join Market m on p.market.id = m.id where p.id = :productId")
    Optional<Market> findMarketByProductId(@Param("productId") Long productId);
}
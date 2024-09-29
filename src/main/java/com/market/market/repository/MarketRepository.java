package com.market.market.repository;

import com.market.market.entity.Market;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 가게 도메인의 Repository 입니다.
 */
public interface MarketRepository extends JpaRepository<Market, Long>, MarketPagingRepository {

    boolean existsByMarketName(String marketName);

    List<Market> findAllByMemberId(Long memberId);
}
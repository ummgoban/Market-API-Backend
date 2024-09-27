package com.market.market.repository;

import com.market.market.entity.MarketImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 가게 이미지 도메인의 Repository 입니다.
 */
public interface MarketImageRepository extends JpaRepository<MarketImage, Long> {

    @Query("select m from MarketImage m where m.market.id = :id")
    List<MarketImage> findAllByMarketId(@Param("id") Long marketId);
}
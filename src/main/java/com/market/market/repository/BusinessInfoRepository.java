package com.market.market.repository;

import com.market.market.entity.BusinessInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * 사업자 도메인의 Repository 입니다.
 */
public interface BusinessInfoRepository extends JpaRepository<BusinessInfo, Long> {

    @Query("select b from BusinessInfo b where b.market.id = :id")
    Optional<BusinessInfo> findByMarketId(@Param("id") Long marketId);
}

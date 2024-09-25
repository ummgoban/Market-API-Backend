package com.market.product.repository;

import com.market.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 상품 도메인의 Repository 입니다.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select p from Product p where p.market.id = :id")
    List<Product> findAllByMarketId(@Param("id") Long marketId);
}

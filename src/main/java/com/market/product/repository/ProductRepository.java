package com.market.product.repository;

import com.market.product.entity.Product;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * 상품 도메인의 Repository 입니다.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select p from Product p where p.market.id = :id")
    List<Product> findAllByMarketId(@Param("id") Long marketId);

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "product lock timeout in bucket", value = "3000")}) // 락 휙득을 위해 3초까지 대기
    @Query("select p from Product p where p.id = :productId")
    Optional<Product> findByProductIdInBucket(@Param("productId") Long productId);
}

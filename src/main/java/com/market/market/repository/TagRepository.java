package com.market.market.repository;

import com.market.market.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {

    /**
     * 상품 ID 값으로 해당 상품에 설정된 Tag 엔티티들을 조회한다.
     *
     * @param productId
     * @return
     */
    @Query("select t " +
            "from Tag t " +
            "inner join ProductTag pt on t.id = pt.tag.id " +
            "inner join Product p on pt.product.id = p.id " +
            "where p.id = :productId")
    List<Tag> findAllByProductId(@Param("productId") Long productId);

    List<Tag> findAllByMarketId(@Param("marketId") Long marketId);
}

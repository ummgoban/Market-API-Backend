package com.market.product.repository;

import com.market.product.entity.ProductTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductTagRepository extends JpaRepository<ProductTag, Long> {

    @Modifying
    @Query("delete from ProductTag pt where pt.product.id = :productId and pt.tag.id = :tagId")
    void deleteByProductIdAndTagId(@Param("productId") Long productId,
                                   @Param("tagId") Long tagId);

    @Modifying
    @Query("delete from ProductTag pt where pt.product.id = :productId")
    void deleteAllByProductId(@Param("productId") Long productId);
}

package com.market.bucket.repository;

import com.market.bucket.dto.server.BucketProductDto;
import com.market.bucket.entity.Bucket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * 장바구니 도메인의 Repository 입니다.
 */
public interface BucketRepository extends JpaRepository<Bucket, Long> {

    /**
     * @param memberId bucket, product 테이블을 조인 후, memberId 값으로 필터링
     * @return
     */
    @Query("select new com.market.bucket.dto.server.BucketProductDto(" +
            "product.id," +
            "product.name," +
            "product.productImage," +
            "product.originPrice," +
            "product.discountPrice," +
            "bucket.count) " +
            "from Bucket bucket " +
            "inner join Product product on bucket.product.id = product.id " +
            "where bucket.member.id = :memberId")
    List<BucketProductDto> findAllBucketProductByMemberId(@Param("memberId") Long memberId);

    /**
     * List<> productId 값들의 bucket 튜플을 조회
     *
     * @param productId
     * @return
     */
    @Query("select b from Bucket b where b.member.id = :memberId and b.product.id in :products")
    List<Bucket> findByMemberIdAndProductIdIn(@Param("memberId") Long memberId,
                                              @Param("products") List<Long> productId);

    /**
     * 회원의 장바구니 정보를 조회
     *
     * @param memberId
     * @return
     */
    List<Bucket> findAllByMemberId(Long memberId);

    /**
     * 회원의 장바구니 상품 중, marketId와 다른 가게의 상품의 장바구니 튜플 조회
     *
     * @param memberId
     * @param marketId
     * @return
     */
    @Query("select bucket.id " +
            "from Bucket bucket " +
            "left join Product product on bucket.product.id = product.id " +
            "inner join Market market on (product.market.id = market.id and market.id <> :marketId) " +
            "where bucket.member.id = :memberId")
    List<Long> findAllIdByMarketIdAndMemberId(@Param("memberId") Long memberId,
                                              @Param("marketId") Long marketId);

    /**
     * list id 에 해당하는 pk 값을 가진, bucket 튜플을 삭제
     *
     * @param idList
     */
    @Modifying
    @Query("delete from Bucket bucket where bucket.id in :idList")
    void deleteByIdIn(@Param("idList") List<Long> idList);

    /**
     * 고객 ID, 상품 ID로 버킷 조회
     * @param memberId
     * @param productId
     * @return
     */
    Optional<Bucket> findByMemberIdAndProductId(Long memberId, Long productId);
}

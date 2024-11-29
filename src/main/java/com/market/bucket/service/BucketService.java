package com.market.bucket.service;

import com.market.bucket.dto.response.BucketProductResponse;
import com.market.bucket.dto.server.BucketProductDto;
import com.market.bucket.dto.server.BucketSaveDto;
import com.market.bucket.entity.Bucket;
import com.market.bucket.repository.BucketRepository;
import com.market.core.exception.BucketException;
import com.market.core.exception.MarketException;
import com.market.core.exception.MemberException;
import com.market.core.exception.ProductException;
import com.market.market.entity.Market;
import com.market.market.entity.MarketImage;
import com.market.market.repository.MarketImageRepository;
import com.market.market.repository.MarketRepository;
import com.market.member.entity.Member;
import com.market.member.repository.MemberRepository;
import com.market.product.entity.Product;
import com.market.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.market.core.code.error.BucketErrorCode.NOT_FOUND;
import static com.market.core.code.error.MarketErrorCode.*;
import static com.market.core.code.error.MemberErrorCode.NOT_FOUND_MEMBER_ID;
import static com.market.core.code.error.ProductErrorCode.NOT_FOUND_PRODUCT_ID;
import static com.market.core.code.error.ProductErrorCode.STOCK_NOT_ENOUGH;

@Service
@RequiredArgsConstructor
public class BucketService {

    private final BucketRepository bucketRepository;
    private final MarketRepository marketRepository;
    private final MarketImageRepository marketImageRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    private final Integer FIRST_INDEX = 0;

    /**
     * 장바구니에 담겨는 상품 정보 리스트와 상품의 가게 정보 정보를 조회
     *
     * @param memberId
     * @return
     */
    @Transactional
    public BucketProductResponse findBucket(Long memberId) {

        // 회원의 장바구니 상품들을 조회
        List<BucketProductDto> bucketProducts = bucketRepository.findAllBucketProductByMemberId(memberId);

        // 상품의 가게 정보 조회 (만약 빈 장바구니이면, 바로 반환)
        if (bucketProducts.isEmpty()) {
            return new BucketProductResponse(null, null);
        }

        // 빈 장바구니가 아니라면, 상품의 가게 정보 조회
        Market market = marketRepository.findMarketByProductId(bucketProducts.get(FIRST_INDEX).getId())
                .orElseThrow(() -> new MarketException(NOT_FOUND_MARKET_BY_PRODUCT_ID));

        // 가게의 이미지 정보 조회
        List<MarketImage> marketImages = marketImageRepository.findAllByMarketId(market.getId());

        return BucketProductResponse.from(market, bucketProducts, marketImages);
    }

    /**
     * 현재 장바구니에 담고자 하는 상품의 가게 정보와 장바구니 속 상품의 가게 정보를 비교한다.
     * 가게 정보가 일치하면 or 현재 장바구니가 비어있으면 true, 일치하지 않으면 false
     *
     * @param marketId
     * @param memberId
     * @return
     */
    @Transactional
    public boolean discriminateBucket(Long marketId, Long memberId) {

        // 회원의 장바구니 상품들을 조회
        List<BucketProductDto> bucketProducts = bucketRepository.findAllBucketProductByMemberId(memberId);

        // 상품의 가게 정보 조회 (만약 빈 장바구니이면, 바로 반환)
        if (bucketProducts.isEmpty()) {
            return true;
        }

        // 빈 장바구니가 아니라면, 상품의 가게 정보 조회
        Market market = marketRepository.findMarketByProductId(bucketProducts.get(FIRST_INDEX).getId())
                .orElseThrow(() -> new MarketException(NOT_FOUND_MARKET_BY_PRODUCT_ID));

        return Objects.equals(marketId, market.getId());
    }

    /**
     * 장바구니에 상품을 추가하는 트랜잭션이다.
     * 1. 담고자 하는 상품의 가게와 다른 가게의 상품이 이미 장바구니에 존재할 경우, 기존 상품들을 전부 삭제한다.
     * 2. 담고자 하는 상품이 이미 장바구니에 존재할 경우, 단순 갯수만 변경한다.
     * 3. 담고자 하는 상품이 장바구니에 없는 새로운 상품인 경우, Bucket 튜플을 생성한다.
     *
     * @param memberId
     * @param marketId
     * @param products
     */
    @Transactional
    public void saveBucket(Long memberId, Long marketId, List<BucketSaveDto> products) {

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberException(NOT_FOUND_MEMBER_ID));

        // 1. 장바구니에 담고자 하는 상품의 가게와 다른 가게의 기존 장바구니 상품 >> 삭제할 튜플
        deleteDifferentMarketBucketProduct(memberId, marketId);

        // 2-1. 장바구니에 담고자 하는 상품들의 id 값을 추출
        List<Long> productsId = products.stream().map(BucketSaveDto::getId).toList();
        // 2-2. 상품 id 값들의 장바구니 튜플을 조회
        List<Bucket> buckets = bucketRepository.findByMemberIdAndProductIdIn(memberId, productsId);

        // 2-3. 기존 장바구니 상품과 동일한 상품이 존재하는 경우, 단순 갯수를 추가한다
        if (!buckets.isEmpty()) {
            for (Bucket bucket : buckets) {

                BucketSaveDto dto = products.stream()
                        .filter(bucketSaveDto -> Objects.equals(bucketSaveDto.getId(), bucket.getProduct().getId()))
                        .findAny()
                        .orElse(null);
                // 기존 장바구니 상품의 갯수를 수정
                bucket.plusCount(dto.getCount());
            }
        }

        // 3.기존 장바구니에 없는 상품의 경우, bucket 튜플을 새로 생성한다.
        // 3-1. 기존 장바구니에 존재하는 상품의 id 값들을 추출한다.
        Set<Long> bucketProductIds = buckets.stream().map(bucket -> bucket.getProduct().getId()).collect(Collectors.toSet());

        // 3-2. 기존 장바구니에 존재하는 상품의 id 값이 아닌 BucketSaveDto(장바구니에 담고자 하는 상품) 를 추출한다.
        List<BucketSaveDto> newProducts = products.stream()
                .filter(dto -> !bucketProductIds.contains(dto.getId()))
                .toList();

        // 3-3. 새로운 bucket 튜플을 생성헤 저장한다.
        for (BucketSaveDto bucketSaveDto : newProducts) {
            Product product = productRepository.findById(bucketSaveDto.getId())
                    .orElseThrow(() -> new ProductException(NOT_FOUND_PRODUCT_ID));

            bucketRepository.save(Bucket.builder()
                    .member(member)
                    .product(product)
                    .count(bucketSaveDto.getCount())
                    .build());

        }
    }

    @Transactional
    public void updateBucketProduct(Long memberId, Long productId, Integer count) {
        Bucket bucket = bucketRepository.findByMemberIdAndProductId(memberId, productId).orElseThrow(() -> new BucketException(NOT_FOUND));

        Product product = productRepository.findByProductIdWithPessimisticWrite(productId).orElseThrow(() -> new ProductException(NOT_FOUND_PRODUCT_ID));
        if (product.getStock() < count) {
            throw new ProductException(STOCK_NOT_ENOUGH);
        }
        bucket.plusCount(count);
    }

    @Transactional
    public void deleteBucketProduct(Long memberId, Long productId) {

        Bucket bucket = bucketRepository.findByMemberIdAndProductId(memberId, productId)
                .orElseThrow(() -> new BucketException(NOT_FOUND));
        Product product = productRepository.findByProductIdWithPessimisticWrite(bucket.getProduct().getId())
                .orElseThrow(() -> new ProductException(NOT_FOUND_PRODUCT_ID));

        product.updateProductStock(bucket.getCount());
        bucketRepository.deleteByIdIn(List.of(bucket.getId()));

    }

    private void deleteDifferentMarketBucketProduct(Long memberId, Long marketId) {
        List<Long> deleteBucketId = bucketRepository.findAllIdByMarketIdAndMemberId(memberId, marketId);
        bucketRepository.deleteByIdIn(deleteBucketId);
    }
}

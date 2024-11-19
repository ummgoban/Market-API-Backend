package com.market.product.service;

import com.market.core.code.error.MarketErrorCode;
import com.market.core.code.error.MemberErrorCode;
import com.market.core.code.error.ProductErrorCode;
import com.market.core.exception.MarketException;
import com.market.core.exception.MemberException;
import com.market.core.exception.ProductException;
import com.market.market.entity.Market;
import com.market.market.entity.Tag;
import com.market.market.repository.MarketLikeRepository;
import com.market.market.repository.MarketRepository;
import com.market.market.repository.TagRepository;
import com.market.member.entity.Member;
import com.market.member.repository.MemberRepository;
import com.market.product.dto.request.ProductCreateRequest;
import com.market.product.dto.request.ProductUpdateRequest;
import com.market.product.dto.response.ProductResponse;
import com.market.product.entity.Product;
import com.market.product.entity.ProductStatus;
import com.market.product.entity.ProductTag;
import com.market.product.repository.ProductRepository;
import com.market.product.repository.ProductTagRepository;
import com.market.utils.fcm.FCMUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final MarketRepository marketRepository;
    private final MemberRepository memberRepository;
    private final MarketLikeRepository marketLikeRepository;
    private final TagRepository tagRepository;
    private final ProductTagRepository productTagRepository;

    private final FCMUtil fcmUtil;


    /**
     * 상품을 등록합니다.
     */
    @Transactional
    public void createProduct(Long memberId, Long marketId, ProductCreateRequest productCreateRequest) {
        Market market = marketRepository.findById(marketId)
                .orElseThrow(() -> new MarketException(MarketErrorCode.NOT_FOUND_MARKET_ID));

        // 가게 소유자가 맞는지 확인
        verifyOwnerOfMarket(memberId, marketId);

        Product product = Product.builder()
                .market(market)
                .productStatus(ProductStatus.IN_STOCK)
                .productImage(productCreateRequest.getProductImage())
                .name(productCreateRequest.getName())
                .originPrice(productCreateRequest.getOriginPrice())
                .discountPrice(productCreateRequest.getDiscountPrice())
                .discountRate(productCreateRequest.getDiscountRate())
                .stock(productCreateRequest.getStock())
                .build();

        List<Tag> marketTags = tagRepository.findAllByMarketId(marketId);

        for (String name : productCreateRequest.getProductTags()) {

            Tag tag;
            boolean exist = marketTags.stream().anyMatch(marketTag -> marketTag.getName().equals(name));
            // 기존 가게에 없던 tag인 경우,
            if (!exist) {

                tag = Tag.builder()
                        .market(market)
                        .name(name)
                        .build();

                tagRepository.save(tag);
            } else {
                tag = marketTags.stream()
                        .filter(marketTag -> marketTag.getName().equals(name))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("상품 등록 INTERNAL SERVER ERROR"));
            }

            productTagRepository.save(ProductTag.builder()
                    .product(product)
                    .tag(tag)
                    .build());
        }

        productRepository.save(product);

        // 새로운 메뉴 업데이트 알림 전송
        sendProductCreatedAlarm(market);

    }

    /**
     * 상품 목록을 조회합니다.
     */
    @Transactional(readOnly = true)
    public List<ProductResponse> getProducts(Long marketId) {
        List<Product> productList = productRepository.findAllByMarketId(marketId);

        return productList.stream()
                .map(product -> {

                    List<Tag> tags = tagRepository.findAllByProductId(product.getId());
                    return ProductResponse.from(product, tags);

                })
                .collect(Collectors.toList());
    }

    /**
     * 상품을 수정합니다.
     */
    @Transactional
    public void updateProduct(Long memberId, Long productId, ProductUpdateRequest productUpdateRequest) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(ProductErrorCode.NOT_FOUND_PRODUCT_ID));

        Market market = marketRepository.findMarketByProductId(productId)
                .orElseThrow(() -> new MarketException(MarketErrorCode.NOT_FOUND_MARKET_ID));

        // 가게 소유자가 맞는지 확인
        verifyOwnerOfMarket(memberId, market.getId());

        // 기존 product tag 삭제
        productTagRepository.deleteAllByProductId(productId);


        List<String> productTagNames = productUpdateRequest.getProductTags();
        List<Tag> marketTags = tagRepository.findAllByMarketId(product.getMarket().getId());


        for (String name : productTagNames) {
            boolean exist = marketTags.stream().anyMatch(productTag -> productTag.getName().equals(name));
            // 기존 market에 있던 tag인 경우,
            if (exist) {
                Tag tag = marketTags.stream()
                        .filter(marketTag -> marketTag.getName().equals(name))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("상품 수정 INTERNAL SERVER ERROR"));

                productTagRepository.save(ProductTag.builder()
                        .product(product)
                        .tag(tag)
                        .build());
            } else {
                // 기존 market에 없던 tag인 경우,
                Tag tag = Tag.builder()
                        .market(market)
                        .name(name)
                        .build();

                tagRepository.save(tag);
                productTagRepository.save(ProductTag.builder()
                        .product(product)
                        .tag(tag)
                        .build());
            }
        }

        product.updateProduct(productUpdateRequest);

        // 재고 업데이트 알림 전송
        sendProductUpdatedAlarm(market);
    }

    /**
     * 상품을 삭제합니다.
     */
    @Transactional
    public void deleteProduct(Long memberId, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(ProductErrorCode.NOT_FOUND_PRODUCT_ID));

        // 가게 소유자가 맞는지 확인
        verifyOwnerOfMarket(memberId, product.getMarket().getId());

        productTagRepository.deleteAllByProductId(product.getId());
        productRepository.delete(product);
    }

    /**
     * 가게 소유지가 맞는지 확인합니다.
     */
    private void verifyOwnerOfMarket(Long memberId, Long marketId) {
        Market market = marketRepository.findById(marketId)
                .orElseThrow(() -> new MarketException(MarketErrorCode.NOT_FOUND_MARKET_ID));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER_ID));

        if (!market.getMember().equals(member)) {
            throw new MarketException(MarketErrorCode.NOT_OWN_STORE);
        }
    }

    /**
     * 상품 등록 시, 찜한 회원들에게 알림을 보낸다
     */
    private void sendProductCreatedAlarm(Market market) {
        List<String> memberDeviceTokens = marketLikeRepository.findMemberDeviceTokens(market.getId());

        if (!memberDeviceTokens.isEmpty()) {
            fcmUtil.sendCreatedAlarms(market.getMarketName(), memberDeviceTokens);
        }
    }

    /**
     * 상품 수정 시, 찜한 회원들에게 알림을 보낸다
     */
    private void sendProductUpdatedAlarm(Market market) {
        List<String> memberDeviceTokens = marketLikeRepository.findMemberDeviceTokens(market.getId());

        if (!memberDeviceTokens.isEmpty()) {
            fcmUtil.sendUpdatedAlarms(market.getMarketName(), memberDeviceTokens);
        }
    }
}

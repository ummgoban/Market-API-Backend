package com.market.product.service;

import com.market.core.code.error.MarketErrorCode;
import com.market.core.code.error.ProductErrorCode;
import com.market.core.exception.MarketException;
import com.market.core.exception.ProductException;
import com.market.market.entity.Market;
import com.market.market.repository.MarketRepository;
import com.market.product.dto.request.ProductCreateRequest;
import com.market.product.dto.request.ProductUpdateRequest;
import com.market.product.dto.response.ProductResponse;
import com.market.product.entity.Product;
import com.market.product.entity.ProductStatus;
import com.market.product.repository.ProductRepository;
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

    /**
     * 상품을 등록합니다.
     */
    @Transactional
    public void createProduct(Long marketId, ProductCreateRequest productCreateRequest) {
        Market market = marketRepository.findById(marketId)
                .orElseThrow(() -> new MarketException(MarketErrorCode.NOT_FOUND_MARKET_ID));

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

        productRepository.save(product);
    }

    /**
     * 상품 목록을 조회합니다.
     */
    @Transactional(readOnly = true)
    public List<ProductResponse> getProducts(Long marketId) {
        List<Product> productList = productRepository.findAllByMarketId(marketId);

        return productList.stream()
                .map(product -> ProductResponse.builder()
                        .id(product.getId())
                        .image(product.getProductImage())
                        .name(product.getName())
                        .originPrice(product.getOriginPrice())
                        .discountPrice(product.getDiscountPrice())
                        .discountRate(product.getDiscountRate())
                        .productStatus(product.getProductStatus())
                        .stock(product.getStock())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * 상품을 수정합니다.
     */
    @Transactional
    public void updateProduct(Long productId, ProductUpdateRequest productUpdateRequest) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(ProductErrorCode.NOT_FOUND_PRODUCT_ID));

        product.updateProduct(productUpdateRequest);
    }
}

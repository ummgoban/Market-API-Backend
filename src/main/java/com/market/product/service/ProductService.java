package com.market.product.service;

import com.market.core.code.error.MarketErrorCode;
import com.market.core.exception.MarketException;
import com.market.market.entity.Market;
import com.market.market.repository.MarketRepository;
import com.market.product.dto.request.ProductCreateRequest;
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
}

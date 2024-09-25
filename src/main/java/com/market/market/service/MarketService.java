package com.market.market.service;

import com.market.core.exception.BusinessInfoException;
import com.market.core.exception.MarketException;
import com.market.market.dto.response.MarketSpecificResponse;
import com.market.market.entity.BusinessInfo;
import com.market.market.entity.Market;
import com.market.market.entity.MarketImage;
import com.market.market.repository.BusinessInfoRepository;
import com.market.market.repository.MarketImageRepository;
import com.market.market.repository.MarketRepository;
import com.market.market.repository.TagRepository;
import com.market.product.dto.response.ProductResponse;
import com.market.product.entity.Product;
import com.market.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.market.core.code.error.BusinessInfoErrorCode.*;
import static com.market.core.code.error.MarketErrorCode.*;

/**
 * 가게 비즈니스 로직 처리 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class MarketService {

    private final MarketRepository marketRepository;
    private final BusinessInfoRepository businessInfoRepository;
    private final MarketImageRepository marketImageRepository;
    private final ProductRepository productRepository;
    private final TagRepository tagRepository;

    /**
     * 가게 상세 조회 트랜잭션입니다.
     *
     * @param marketId 가게 아이디입니다.
     * @return MarketSpecificResponse
     */
    @Transactional(readOnly = true)
    public MarketSpecificResponse findSpecificMarket(Long marketId) {
        // dev 머지 후, exception handler 구현
        Market market = marketRepository.findById(marketId).orElseThrow(() -> new MarketException(NOT_FOUND_MARKET_ID));

        // 가게 사업자 조회
        BusinessInfo businessInfo = businessInfoRepository.findByMarketId(marketId).
                orElseThrow(() -> new BusinessInfoException(NOT_FOUND_Business_Info_ID));

        // 가게 이미지들 조회2
        List<MarketImage> marketImages = marketImageRepository.findAllByMarketId(marketId);

        // 가게 상품들 조회
        List<Product> products = productRepository.findAllByMarketId(marketId);

        // 가게의 상품 정보를 담을 배열 선언
        List<ProductResponse> productResponses = new ArrayList<>();

        // 상품 각각에 대해 태그 정보 조회 후, productResponse 객체 생성 후 productResponses에 저장
        for (Product product : products) {
            List<String> tagNames = tagRepository.findAllByProductId(product.getId());

            ProductResponse productResponse = ProductResponse.from(product, tagNames);
            productResponses.add(productResponse);
        }

        return MarketSpecificResponse.from(market, businessInfo, marketImages, productResponses);
    }
}

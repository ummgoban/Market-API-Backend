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
import com.market.product.entity.Product;
import com.market.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * 가게 상세 조회 트랜잭션입니다.
     * @param marketId 가게 아이디입니다.
     * @return MarketSpecificResponse
     */
    @Transactional(readOnly = true)
    public MarketSpecificResponse findSpecificMarket(Long marketId) {
        // dev 머지 후, exception handler 구현
        Market market = marketRepository.findById(marketId).orElseThrow(() -> new MarketException(NOT_FOUND_MARKET_ID));

        BusinessInfo businessInfo = businessInfoRepository.findByMarketId(marketId).
                orElseThrow(() -> new BusinessInfoException(NOT_FOUND_Business_Info_ID));

        List<MarketImage> marketImages = marketImageRepository.findAllByMarketId(marketId);

        List<Product> products = productRepository.findAllByMarketId(marketId);

        return MarketSpecificResponse.from(market, businessInfo, marketImages, products);
    }
}

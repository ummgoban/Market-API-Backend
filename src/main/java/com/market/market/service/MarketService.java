package com.market.market.service;

import com.market.core.code.error.MarketErrorCode;
import com.market.core.code.error.MemberErrorCode;
import com.market.core.exception.MarketException;
import com.market.core.exception.MemberException;
import com.market.core.security.principal.PrincipalDetails;
import com.market.market.dto.request.MarketRegisterRequest;
import com.market.market.dto.server.BusinessStatusResponseDto;
import com.market.market.dto.response.MarketSpecificResponse;
import com.market.market.entity.Market;
import com.market.market.entity.MarketImage;
import com.market.market.repository.MarketImageRepository;
import com.market.market.repository.MarketRepository;
import com.market.member.entity.Member;
import com.market.member.repository.MemberRepository;
import com.market.market.repository.TagRepository;
import com.market.product.dto.response.ProductResponse;
import com.market.product.entity.Product;
import com.market.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

/**
 * 가게 관련 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class MarketService {

    private final MemberRepository memberRepository;
    private final MarketRepository marketRepository;
    private final MarketImageRepository marketImageRepository;
    private final ProductRepository productRepository;
    private final TagRepository tagRepository;

    @Value("${external.api.business-status.url}")
    private String baseUrl;

    @Value("${external.api.business-status.service-key}")
    private String serviceKey;

    /**
     * 가게 상세 조회 트랜잭션입니다.
     */
    @Transactional(readOnly = true)
    public MarketSpecificResponse findSpecificMarket(Long marketId) {
        // dev 머지 후, exception handler 구현
        Market market = marketRepository.findById(marketId).orElseThrow(() -> new MarketException(MarketErrorCode.NOT_FOUND_MARKET_ID));

        // 가게 이미지들 조회
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

        return MarketSpecificResponse.from(market, marketImages, productResponses);
    }

    /**
     * 매장 등록
     */
    public Long registerMarket(PrincipalDetails principalDetails, MarketRegisterRequest marketRegisterRequest) {
        // 회원 조회
        Member member = memberRepository.findById(Long.parseLong(principalDetails.getUsername()))
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER_ID));

        // 매장 이름 중복 체크
        if (marketRepository.existsByMarketName(marketRegisterRequest.getMarketName())) {
            throw new MarketException(MarketErrorCode.DUPLICATE_MARKET_NAME);
        }

        // 사업자 등록 번호 유효성 검증
        BusinessStatusResponseDto businessStatusResponseDto = getBusinessStatus(marketRegisterRequest.getBusinessNumber());
        String taxType = businessStatusResponseDto.getData().get(0).getTaxType();
        if (!isValidBusinessNumber(taxType)) {
            throw new MarketException(MarketErrorCode.INVALID_BUSINESS_NUMBER);
        }

        Market market = Market.builder()
                .member(member)
                .marketName(marketRegisterRequest.getMarketName())
                .businessNumber(businessStatusResponseDto.getData().get(0).getBusinessNumber())
                .address(marketRegisterRequest.getAddress())
                .specificAddress(marketRegisterRequest.getSpecificAddress())
                .contactNumber(marketRegisterRequest.getContactNumber())
                .build();

        return marketRepository.save(market).getId();
    }

    /**
     * 사업자 등록 번호 유효성 검증
     */
    public boolean validateBusinessStatus(String businessNumber) {
        BusinessStatusResponseDto businessStatusResponseDto = getBusinessStatus(businessNumber);
        String taxType = businessStatusResponseDto.getData().get(0).getTaxType();

        return isValidBusinessNumber(taxType);
    }

    /**
     * 사업자 번호를 사용하여 외부 API를 통해 사업자 상태 정보를 조회
     */
    private BusinessStatusResponseDto getBusinessStatus(String businessNumber) {

        WebClient webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();

        // URI 생성 (serviceKey는 이미 인코딩되어 있으므로 다시 인코딩하지 않음)
        URI uri = URI.create(
                UriComponentsBuilder.fromHttpUrl(baseUrl)
                        .queryParam("serviceKey", "{serviceKey}")
                        .build(false) // 인코딩하지 않음
                        .expand(serviceKey)
                        .toUriString()
        );

        // 요청 본문 설정 (사업자 번호 배열로 전달)
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("b_no", new String[]{businessNumber});

        BusinessStatusResponseDto response = webClient.post()
                .uri(uri)
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, responseEntity -> Mono.error(new MarketException(MarketErrorCode.BAD_REQUEST_BUSINESS_STATUS)))
                .onStatus(HttpStatusCode::is5xxServerError, responseEntity -> Mono.error(new MarketException(MarketErrorCode.BUSINESS_STATUS_SERVER_ERROR)))
                .bodyToMono(BusinessStatusResponseDto.class)
                .block();

        return response;
    }

    /**
     * 세금 유형을 기반으로 사업자 등록 번호가 유효한지 여부를 확인
     */
    private boolean isValidBusinessNumber(String taxType) {
        return !"국세청에 등록되지 않은 사업자등록번호입니다.".equals(taxType);
    }
}
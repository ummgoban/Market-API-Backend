package com.market.market.service;

import com.market.core.code.error.MarketErrorCode;
import com.market.core.exception.MarketException;
import com.market.market.dto.server.BusinessStatusResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * 사업자 번호를 사용하여 외부 API를 통해 사업자 상태 정보를 조회
 */
@Service
public class BusinessStatusService {

    @Value("${external.api.business-status.url}")
    private String baseUrl;

    @Value("${external.api.business-status.service-key}")
    private String serviceKey;

    public BusinessStatusResponseDto getBusinessStatus(String businessNumber) {

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
}
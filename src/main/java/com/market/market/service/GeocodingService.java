package com.market.market.service;


import com.market.core.exception.GeocodingException;
import com.market.core.security.dto.jwt.server.NaverErrorResponseDto;
import com.market.market.dto.server.GeocodingErrorResponse;
import com.market.market.dto.server.NcpGeocodingInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.market.core.code.error.GeocodingErrorCode.*;

/**
 * Naver Cloud Platform에서 주소 값을 이용해 위도, 경도 값을 추출하는 서비스입니다.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class GeocodingService {

    @Value("${x.ncp.apigw.api-key.id}")
    private String clientKeyId;

    @Value("${x.ncp.apigw.api-key.secret}")
    private String clientKeySecret;

    private final String GEOCODING_URL = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode";

    public NcpGeocodingInfoDto getLatitudeAndLongitude(String address) {

        WebClient webClient = WebClient.builder()
                .baseUrl(GEOCODING_URL)
                .defaultHeader(HttpHeaders.ACCEPT, "application/json")
                .defaultHeader("X-NCP-APIGW-API-KEY-ID", clientKeyId)
                .defaultHeader("X-NCP-APIGW-API-KEY", clientKeySecret)
                .build();

        NcpGeocodingInfoDto ncpGeocodingInfoDto = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("query", address)
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, this::handleClientError)
                .onStatus(HttpStatusCode::is5xxServerError, this::handleServerError)
                .bodyToMono(NcpGeocodingInfoDto.class)
                .block();

        if (ncpGeocodingInfoDto.getAddresses().size() != 1) {
            throw new GeocodingException(INVALID_ADDRESS);
        }

        return ncpGeocodingInfoDto;
    }

    /**
     * Naver Cloud Platform 클라이언트 오류 처리 메서드
     */
    private Mono<? extends Throwable> handleClientError(ClientResponse clientResponse) {
        int statusCode = clientResponse.statusCode().value();
        return clientResponse.bodyToMono(GeocodingErrorResponse.class)
                .flatMap(errorResponse -> {
                    String errorCode = errorResponse.getError().getErrorCode();
                    return switch (errorCode) {
                        case "100" -> Mono.error(new GeocodingException(BAD_REQUEST_EXCEPTION));
                        case "200" -> Mono.error(new GeocodingException(AUTHENTICATION_FAILED));
                        case "210" -> Mono.error(new GeocodingException(PERMISSION_DENIED));
                        case "300" -> Mono.error(new GeocodingException(NOT_FOUND_EXCEPTION));
                        case "400" -> Mono.error(new GeocodingException(QUOTA_EXCEEDED));
                        case "410" -> Mono.error(new GeocodingException(THROTTLE_LIMITED));
                        case "420" -> Mono.error(new GeocodingException(RATE_LIMITED));
                        case "430" -> Mono.error(new GeocodingException(REQUEST_ENTITY_TOO_LARGE));
                        default -> Mono.error(new GeocodingException(UNEXPECTED_CLIENT_ERROR));
                    };
                });
    }

    /**
     * Naver Cloud Platform 서버 오류 처리 메서드
     */
    private Mono<? extends Throwable> handleServerError(ClientResponse clientResponse) {
        return clientResponse.bodyToMono(NaverErrorResponseDto.class)
                .flatMap(errorResponse -> {
                    String errorCode = errorResponse.getErrorCode();
                    return switch (errorCode) {
                        case "500" -> Mono.error(new GeocodingException(ENDPOINT_ERROR));
                        case "510" -> Mono.error(new GeocodingException(ENDPOINT_TIMEOUT));
                        default -> Mono.error(new GeocodingException(UNEXPECTED_SERVER_ERROR));
                    };
                });
    }
}

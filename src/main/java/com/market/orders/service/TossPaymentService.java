package com.market.orders.service;


import com.market.core.code.error.PaymentErrorCode;
import com.market.core.exception.PaymentException;
import com.market.orders.dto.server.PaymentConfirmRequestDto;
import com.market.orders.dto.server.PaymentInfoResponseDto;
import com.market.orders.dto.server.TossPaymentErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Base64;


@Service
@RequiredArgsConstructor
public class TossPaymentService {

    @Value("${toss.payment.secret}")
    private String tossPaymentSecret;

    @Value("${toss.payment.idempotency}")
    private String tossPaymentIdempotency;

    private final String PAYMENT_CONFIRM_URL = "https://api.tosspayments.com/v1/payments/confirm";

    public PaymentInfoResponseDto confirmPayment(String paymentKey, String ordersId, Integer amount) {

        WebClient webClient = WebClient.builder()
                .baseUrl(PAYMENT_CONFIRM_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Basic " + encodeTossSecret(tossPaymentSecret))
//                .defaultHeader("Idempotency-Key", tossPaymentIdempotency)
                .build();

        PaymentInfoResponseDto paymentInfoResponseDto = webClient.post()
                .bodyValue(new PaymentConfirmRequestDto(amount, ordersId, paymentKey))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, this::handleClientError)
                .onStatus(HttpStatusCode::is5xxServerError, this::handleServerError)
                .bodyToMono(PaymentInfoResponseDto.class)
                .block();

        return paymentInfoResponseDto;
    }

    private String encodeTossSecret(String secretKey) {
        // 시크릿 키 뒤에 ":" 추가
        String keyWithColon = secretKey + ":";

        return Base64.getEncoder().encodeToString(keyWithColon.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 토스 페이먼츠 클라이언트 오류 처리 메서드
     */
    private Mono<? extends Throwable> handleClientError(ClientResponse clientResponse) {

        return clientResponse.bodyToMono(TossPaymentErrorResponse.class)
                .flatMap(errorResponse -> {
                    String code = errorResponse.getCode();
                    return switch (code) {
                        case "ALREADY_PROCESSED_PAYMENT" ->
                                Mono.error(new PaymentException(PaymentErrorCode.ALREADY_PROCESSED_PAYMENT));
                        case "EXCEED_MAX_CARD_INSTALLMENT_PLAN" ->
                                Mono.error(new PaymentException(PaymentErrorCode.EXCEED_MAX_CARD_INSTALLMENT_PLAN));
                        case "INVALID_REQUEST" -> Mono.error(new PaymentException(PaymentErrorCode.INVALID_REQUEST));
                        case "NOT_ALLOWED_POINT_USE" ->
                                Mono.error(new PaymentException(PaymentErrorCode.NOT_ALLOWED_POINT_USE));
                        case "INVALID_REJECT_CARD" ->
                                Mono.error(new PaymentException(PaymentErrorCode.INVALID_REJECT_CARD));
                        case "BELOW_MINIMUM_AMOUNT" ->
                                Mono.error(new PaymentException(PaymentErrorCode.BELOW_MINIMUM_AMOUNT));
                        case "INVALID_CARD_EXPIRATION" ->
                                Mono.error(new PaymentException(PaymentErrorCode.INVALID_CARD_EXPIRATION));
                        case "INVALID_STOPPED_CARD" ->
                                Mono.error(new PaymentException(PaymentErrorCode.INVALID_STOPPED_CARD));
                        case "EXCEED_MAX_DAILY_PAYMENT_COUNT" ->
                                Mono.error(new PaymentException(PaymentErrorCode.EXCEED_MAX_DAILY_PAYMENT_COUNT));
                        case "NOT_SUPPORTED_INSTALLMENT_PLAN_CARD_OR_MERCHANT" ->
                                Mono.error(new PaymentException(PaymentErrorCode.NOT_SUPPORTED_INSTALLMENT_PLAN_CARD_OR_MERCHANT));
                        case "INVALID_CARD_INSTALLMENT_PLAN" ->
                                Mono.error(new PaymentException(PaymentErrorCode.INVALID_CARD_INSTALLMENT_PLAN));
                        case "NOT_SUPPORTED_MONTHLY_INSTALLMENT_PLAN" ->
                                Mono.error(new PaymentException(PaymentErrorCode.NOT_SUPPORTED_MONTHLY_INSTALLMENT_PLAN));
                        case "EXCEED_MAX_PAYMENT_AMOUNT" ->
                                Mono.error(new PaymentException(PaymentErrorCode.EXCEED_MAX_PAYMENT_AMOUNT));
                        case "NOT_FOUND_TERMINAL_ID" ->
                                Mono.error(new PaymentException(PaymentErrorCode.NOT_FOUND_TERMINAL_ID));
                        case "INVALID_CARD_LOST_OR_STOLEN" ->
                                Mono.error(new PaymentException(PaymentErrorCode.INVALID_CARD_LOST_OR_STOLEN));
                        case "RESTRICTED_TRANSFER_ACCOUNT" ->
                                Mono.error(new PaymentException(PaymentErrorCode.RESTRICTED_TRANSFER_ACCOUNT));
                        case "INVALID_CARD_NUMBER" ->
                                Mono.error(new PaymentException(PaymentErrorCode.INVALID_CARD_NUMBER));
                        case "INVALID_UNREGISTERED_SUBMALL" ->
                                Mono.error(new PaymentException(PaymentErrorCode.INVALID_UNREGISTERED_SUBMALL));
                        case "NOT_REGISTERED_BUSINESS" ->
                                Mono.error(new PaymentException(PaymentErrorCode.NOT_REGISTERED_BUSINESS));
                        case "EXCEED_MAX_ONE_DAY_WITHDRAW_AMOUNT" ->
                                Mono.error(new PaymentException(PaymentErrorCode.EXCEED_MAX_ONE_DAY_WITHDRAW_AMOUNT));
                        case "EXCEED_MAX_ONE_TIME_WITHDRAW_AMOUNT" ->
                                Mono.error(new PaymentException(PaymentErrorCode.EXCEED_MAX_ONE_TIME_WITHDRAW_AMOUNT));
                        case "EXCEED_MAX_AMOUNT" ->
                                Mono.error(new PaymentException(PaymentErrorCode.EXCEED_MAX_AMOUNT));
                        case "INVALID_ACCOUNT_INFO_RE_REGISTER" ->
                                Mono.error(new PaymentException(PaymentErrorCode.INVALID_ACCOUNT_INFO_RE_REGISTER));
                        case "NOT_AVAILABLE_PAYMENT" ->
                                Mono.error(new PaymentException(PaymentErrorCode.NOT_AVAILABLE_PAYMENT));
                        case "UNAPPROVED_ORDER_ID" ->
                                Mono.error(new PaymentException(PaymentErrorCode.UNAPPROVED_ORDER_ID));
                        case "EXCEED_MAX_MONTHLY_PAYMENT_AMOUNT" ->
                                Mono.error(new PaymentException(PaymentErrorCode.EXCEED_MAX_MONTHLY_PAYMENT_AMOUNT));
                        case "REJECT_ACCOUNT_PAYMENT" ->
                                Mono.error(new PaymentException(PaymentErrorCode.REJECT_ACCOUNT_PAYMENT));
                        case "REJECT_CARD_PAYMENT" ->
                                Mono.error(new PaymentException(PaymentErrorCode.REJECT_CARD_PAYMENT));
                        case "INVALID_PASSWORD" -> Mono.error(new PaymentException(PaymentErrorCode.INVALID_PASSWORD));
                        case "UNAUTHORIZED_KEY" -> Mono.error(new PaymentException(PaymentErrorCode.UNAUTHORIZED_KEY));
                        case "REJECT_CARD_COMPANY" ->
                                Mono.error(new PaymentException(PaymentErrorCode.REJECT_CARD_COMPANY));
                        case "FORBIDDEN_REQUEST" ->
                                Mono.error(new PaymentException(PaymentErrorCode.FORBIDDEN_REQUEST));
                        case "REJECT_TOSSPAY_INVALID_ACCOUNT" ->
                                Mono.error(new PaymentException(PaymentErrorCode.REJECT_TOSSPAY_INVALID_ACCOUNT));
                        case "EXCEED_MAX_AUTH_COUNT" ->
                                Mono.error(new PaymentException(PaymentErrorCode.EXCEED_MAX_AUTH_COUNT));
                        case "EXCEED_MAX_ONE_DAY_AMOUNT" ->
                                Mono.error(new PaymentException(PaymentErrorCode.EXCEED_MAX_ONE_DAY_AMOUNT));
                        case "NOT_AVAILABLE_BANK" ->
                                Mono.error(new PaymentException(PaymentErrorCode.NOT_AVAILABLE_BANK));
                        case "FDS_ERROR" -> Mono.error(new PaymentException(PaymentErrorCode.FDS_ERROR));
                        case "NOT_FOUND_PAYMENT" ->
                                Mono.error(new PaymentException(PaymentErrorCode.NOT_FOUND_PAYMENT));
                        default -> Mono.error(new PaymentException(PaymentErrorCode.NOT_FOUND_PAYMENT_SESSION));
                    };
                });
    }

    /**
     * 토스 페이먼츠 서버 오류 처리 메서드
     */
    private Mono<? extends Throwable> handleServerError(ClientResponse clientResponse) {
        return clientResponse.bodyToMono(TossPaymentErrorResponse.class)
                .flatMap(errorResponse -> {
                    String code = errorResponse.getCode();
                    return switch (code) {
                        case "FAILED_PAYMENT_INTERNAL_SYSTEM_PROCESSING" ->
                                Mono.error(new PaymentException(PaymentErrorCode.FAILED_PAYMENT_INTERNAL_SYSTEM_PROCESSING));
                        case "FAILED_INTERNAL_SYSTEM_PROCESSING" ->
                                Mono.error(new PaymentException(PaymentErrorCode.FAILED_INTERNAL_SYSTEM_PROCESSING));
                        case "PROVIDER_ERROR" -> Mono.error(new PaymentException(PaymentErrorCode.PROVIDER_ERROR));
                        case "INVALID_API_KEY" -> Mono.error(new PaymentException(PaymentErrorCode.INVALID_API_KEY));
                        case "INVALID_AUTHORIZE_AUTH" ->
                                Mono.error(new PaymentException(PaymentErrorCode.INVALID_AUTHORIZE_AUTH));
                        case "CARD_PROCESSING_ERROR" ->
                                Mono.error(new PaymentException(PaymentErrorCode.CARD_PROCESSING_ERROR));
                        case "INCORRECT_BASIC_AUTH_FORMAT" ->
                                Mono.error(new PaymentException(PaymentErrorCode.INCORRECT_BASIC_AUTH_FORMAT));
                        default -> Mono.error(new PaymentException(PaymentErrorCode.UNKNOWN_PAYMENT_ERROR));
                    };
                });
    }

}

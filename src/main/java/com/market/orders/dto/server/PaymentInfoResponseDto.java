package com.market.orders.dto.server;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * 결제 승인 성공 응답 DTO 클래스입니다.
 */
@Getter
public class PaymentInfoResponseDto {

//    @JsonProperty("mId")
//    private String
//    @JsonProperty("lastTransactionKey")
//    private String
    @JsonProperty("paymentKey")
    private String paymentKey;
    @JsonProperty("orderId")
    private String orderId;
    @JsonProperty("orderName")
    private String orderName;
//    @JsonProperty("taxExemptionAmount")
//    private Integer
//    @JsonProperty("status")
//    private String
//    @JsonProperty("requestedAt")
//    private String
    @JsonProperty("approvedAt")
    private String approvedAt;
//    @JsonProperty("useEscrow")
//    private Boolean
//    @JsonProperty("cultureExpense")
//    private Boolean
//    @JsonProperty("virtualAccount")
//    @JsonProperty("transfer")
//    @JsonProperty("mobilePhone")
//    @JsonProperty("giftCertificate")
//    @JsonProperty("cashReceipt")
//    @JsonProperty("cashReceipts")
//    @JsonProperty("discount")
//    @JsonProperty("cancels")
//    @JsonProperty("secret")
//    @JsonProperty("type")
//    private String
//    @JsonProperty("easyPayAmount")
//    private Integer
//    @JsonProperty("easyPayDiscountAmount")
//    private Integer
//    @JsonProperty("country")
//    private String
//    @JsonProperty("failure")
//    @JsonProperty("isPartialCancelable")
//    @JsonProperty("currency")
//    private String
    @JsonProperty("totalAmount")
    private Integer totalAmount;
//    @JsonProperty("balanceAmount")
//    private Integer
//    @JsonProperty("suppliedAmount")
//    private Integer
//    @JsonProperty("vat")
//    private Integer
//    @JsonProperty("taxFreeAmount")
//    private Integer
    @JsonProperty("method")
    private String method;
//    @JsonProperty("version")
//    private String


}

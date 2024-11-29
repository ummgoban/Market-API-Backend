package com.market.orders.dto.server;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentConfirmRequestDto {

    private Integer amount;

    private String orderId;

    private String paymentKey;

}

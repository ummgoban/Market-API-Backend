package com.market.orders.service;

import com.market.core.exception.MemberException;
import com.market.core.exception.OrdersException;
import com.market.member.entity.Member;
import com.market.member.repository.MemberRepository;
import com.market.orders.dto.server.PaymentInfoResponseDto;
import com.market.orders.entity.Orders;
import com.market.orders.entity.OrdersStatus;
import com.market.orders.entity.Payment;
import com.market.orders.entity.PaymentMethod;
import com.market.orders.repository.OrdersRepository;
import com.market.orders.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.market.core.code.error.MemberErrorCode.NOT_FOUND_MEMBER_ID;
import static com.market.core.code.error.OrdersErrorCode.NOT_FOUND_ORDERS_ID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final MemberRepository memberRepository;
    private final OrdersRepository ordersRepository;
    private final PaymentRepository paymentRepository;

    private final TossPaymentService tossPaymentService;

    @Transactional
    public void confirmPayment(Long memberId, String paymentKey, String ordersId, Integer amount) {

        // 토스에 결제 승인 요청
        PaymentInfoResponseDto paymentInfoResponseDto = tossPaymentService.confirmPayment(paymentKey, ordersId, amount);

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberException(NOT_FOUND_MEMBER_ID));
        Orders orders = ordersRepository.findById(ordersId).orElseThrow(() -> new OrdersException(NOT_FOUND_ORDERS_ID));

        paymentRepository.save(Payment.builder()
                .id(paymentKey)
                .orders(orders)
                .member(member)
                .approvedAt(LocalDateTime.parse(paymentInfoResponseDto.getApprovedAt()))
                .totalAmount(paymentInfoResponseDto.getTotalAmount())
                .method(PaymentMethod.getFromString(paymentInfoResponseDto.getMethod()))
                .build());

        orders.updateOrdersStatus(OrdersStatus.ORDERED);
    }
}

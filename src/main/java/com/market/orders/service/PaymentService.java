package com.market.orders.service;

import com.market.core.code.error.PaymentErrorCode;
import com.market.core.exception.MemberException;
import com.market.core.exception.OrdersException;
import com.market.core.exception.PaymentException;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.market.core.code.error.MemberErrorCode.NOT_FOUND_MEMBER_ID;
import static com.market.core.code.error.OrdersErrorCode.NOT_FOUND_ORDERS_ID;
import static com.market.core.code.error.PaymentErrorCode.INVALID_ORDERS_ID_AND_AMOUNT;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final MemberRepository memberRepository;
    private final OrdersRepository ordersRepository;
    private final PaymentRepository paymentRepository;

    private final TossPaymentService tossPaymentService;

    @Transactional
    public void confirmPayment(Long memberId, String paymentKey, String ordersId, Integer amount) {

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberException(NOT_FOUND_MEMBER_ID));
        Orders orders = ordersRepository.findById(ordersId).orElseThrow(() -> new OrdersException(NOT_FOUND_ORDERS_ID));

        log.info("paymentKey = " + paymentKey);

        log.info("수신한 ordersId = " + ordersId);
        log.info("기존 저장된 ordersId = " + orders.getId());

        log.info("수신한 금액 = " + amount);
        log.info("기존 저장된 주문 금액 = " + orders.getOrdersPrice());

        if (!Objects.equals(orders.getOrdersPrice(), amount)) {
            throw new PaymentException(INVALID_ORDERS_ID_AND_AMOUNT);
        }
        // 토스에 결제 승인 요청
        PaymentInfoResponseDto paymentInfoResponseDto = tossPaymentService.confirmPayment(paymentKey, ordersId, amount);

        paymentRepository.save(Payment.builder()
                .id(paymentKey)
                .orders(orders)
                .member(member)
                .approvedAt(LocalDateTime.parse(paymentInfoResponseDto.getApprovedAt().substring(0, 19)))
                .totalAmount(paymentInfoResponseDto.getTotalAmount())
                .method(PaymentMethod.getFromString(paymentInfoResponseDto.getMethod()))
                .build());

        orders.updateOrdersStatus(OrdersStatus.ORDERED);
    }

}

package com.market.orders.service;


import com.market.core.exception.MemberException;
import com.market.member.entity.Member;
import com.market.member.repository.MemberRepository;
import com.market.orders.dto.request.MarketOrdersRequest;
import com.market.orders.dto.response.MarketOrderedOrdersResponse;
import com.market.orders.dto.server.OrdersProductsDto;
import com.market.orders.entity.Orders;
import com.market.orders.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.market.core.code.error.MemberErrorCode.NOT_FOUND_MEMBER_ID;

@Service
@RequiredArgsConstructor
public class OrdersReadService {

    private final OrdersRepository ordersRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public List<MarketOrderedOrdersResponse> getMarketOrderedOrders(MarketOrdersRequest request) {
        List<MarketOrderedOrdersResponse> response = new ArrayList<>();

        // 가게의 접수 or 픽업 대기 or 픽업완료/취소 주문 목록 조회
        List<Orders> orderedOrders = ordersRepository.getMarketOrderedOrdersByMarketIdAndOrdersStatus(request.getMarketId(), MarketOrdersRequest.from(request));

        orderedOrders.forEach(orders -> {
            // 주문자명 조회
            Member member = memberRepository.findById(orders.getMember().getId()).
                    orElseThrow(() -> new MemberException(NOT_FOUND_MEMBER_ID));

            // 주문 상품 조회
            List<OrdersProductsDto> ordersProducts = ordersRepository.getOrdersProductsDtoByOrdersId(orders.getId());

            response.add(MarketOrderedOrdersResponse.builder()
                    .id(orders.getId())
                    .createdAt(orders.getCreatedAt())
                    .pickupReservedAt(orders.getPickupReservedAt())
                    .ordersPrice(orders.getOrdersPrice())
                    .orderMemberName(member.getName())
                    .products(ordersProducts)
                    .build());
        });

        return response;
    }
}
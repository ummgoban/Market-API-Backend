package com.market.orders.service;


import com.market.core.exception.MarketException;
import com.market.core.exception.MemberException;
import com.market.core.exception.OrdersException;
import com.market.market.entity.Market;
import com.market.market.repository.MarketRepository;
import com.market.member.entity.Member;
import com.market.member.repository.MemberRepository;
import com.market.orders.dto.response.MemberOrdersResponse;
import com.market.orders.dto.response.OrdersResponse;
import com.market.orders.dto.server.OrdersPaymentDto;
import com.market.orders.dto.server.OrdersProductsDto;
import com.market.orders.entity.Orders;
import com.market.orders.entity.OrdersStatus;
import com.market.orders.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.market.core.code.error.MarketErrorCode.NOT_FOUND_MARKET_ID;
import static com.market.core.code.error.MemberErrorCode.NOT_FOUND_MEMBER_ID;
import static com.market.core.code.error.OrdersErrorCode.NOT_FOUND_ORDERS_ID;

@Service
@RequiredArgsConstructor
public class OrdersReadService {

    private final OrdersRepository ordersRepository;
    private final MemberRepository memberRepository;
    private final MarketRepository marketRepository;

    @Transactional(readOnly = true)
    public List<OrdersResponse> getMarketOrders(List<OrdersStatus> ordersStatus, Long marketId) {
        List<OrdersResponse> response = new ArrayList<>();

        // 가게의 접수 or 픽업 대기 or 픽업완료/취소 주문 목록 조회
        List<Orders> orderedOrders = ordersRepository.getMarketOrdersByMarketIdAndOrdersStatus(marketId, ordersStatus);

        orderedOrders.forEach(orders -> {
            // 주문자명 조회
            Member member = memberRepository.findById(orders.getMember().getId()).
                    orElseThrow(() -> new MemberException(NOT_FOUND_MEMBER_ID));

            // 주문 상품 조회
            List<OrdersProductsDto> ordersProducts = ordersRepository.getOrdersProductsDtoByOrdersId(orders.getId());

            response.add(OrdersResponse.builder()
                    .id(orders.getId())
                    .createdAt(orders.getCreatedAt())
                    .doneAt(orders.getDoneAt())
                    .pickupReservedAt(orders.getPickupReservedAt())
                    .ordersPrice(orders.getOrdersPrice())
                    .orderMemberName(member.getName())
                    .ordersStatus(orders.getOrdersStatus())
                    .customerRequest(orders.getCustomerRequest())
                    .products(ordersProducts)
                    .build());
        });

        return response;
    }

    @Transactional(readOnly = true)
    public OrdersResponse getOrder(String orderId) {
        OrdersPaymentDto ordersPaymentDto = ordersRepository.getOrdersPaymentByOrdersId(orderId).orElseThrow(() -> new OrdersException(NOT_FOUND_ORDERS_ID));

        // 주문 상품 조회
        List<OrdersProductsDto> ordersProducts = ordersRepository.getOrdersProductsDtoByOrdersId(ordersPaymentDto.getId());

        return OrdersResponse.from(ordersPaymentDto, ordersProducts);
    }

    @Transactional(readOnly = true)
    public List<OrdersResponse> getMemberOrdersInProgress(Long memberId) {
        List<OrdersResponse> response = new ArrayList<>();

        List<Orders> memberOrders = ordersRepository.
                getMemberOrdersByMemberIdAndOrdersStatus(memberId, OrdersStatus.getInProgressOrderStatus());

        return getOrdersResponses(response, memberOrders);
    }

    @Transactional(readOnly = true)
    public List<MemberOrdersResponse> getMemberOrders(Long memberId) {
        List<MemberOrdersResponse> response = new ArrayList<>();

        List<Orders> memberOrders = ordersRepository.getMemberOrdersByMemberId(memberId);

        return getMemberOrdersResponses(response, memberOrders);

    }

    private List<OrdersResponse> getOrdersResponses(List<OrdersResponse> response, List<Orders> memberOrders) {
        memberOrders.forEach(orders -> {

            // 주문 상품 조회
            List<OrdersProductsDto> ordersProducts = ordersRepository.getOrdersProductsDtoByOrdersId(orders.getId());

            response.add(OrdersResponse.builder()
                    .id(orders.getId())
                    .createdAt(orders.getCreatedAt())
                    .doneAt(orders.getDoneAt())
                    .pickupReservedAt(orders.getPickupReservedAt())
                    .ordersPrice(orders.getOrdersPrice())
                    .ordersStatus(orders.getOrdersStatus())
                    .customerRequest(orders.getCustomerRequest())
                    .products(ordersProducts)
                    .build());
        });

        return response;
    }

    private List<MemberOrdersResponse> getMemberOrdersResponses(List<MemberOrdersResponse> response, List<Orders> memberOrders) {
        memberOrders.forEach(orders -> {

            // 가게 조회
            Market market = marketRepository.findById(orders.getMarket().getId()).orElseThrow(() -> new MarketException(NOT_FOUND_MARKET_ID));
            // 주문 상품 조회
            List<OrdersProductsDto> ordersProducts = ordersRepository.getOrdersProductsDtoByOrdersId(orders.getId());

            response.add(MemberOrdersResponse.builder()
                    .ordersId(orders.getId())
                    .marketId(market.getId())
                    .marketName(market.getMarketName())
                    .createdAt(orders.getCreatedAt())
                    .doneAt(orders.getDoneAt())
                    .pickupReservedAt(orders.getPickupReservedAt())
                    .ordersPrice(orders.getOrdersPrice())
                    .ordersStatus(orders.getOrdersStatus())
                    .customerRequest(orders.getCustomerRequest())
                    .products(ordersProducts)
                    .imageUrl(ordersProducts.get(0).getImage())
                    .build());
        });

        return response;
    }

}

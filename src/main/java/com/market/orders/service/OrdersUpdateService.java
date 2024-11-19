package com.market.orders.service;


import com.market.core.exception.OrdersException;
import com.market.orders.entity.Orders;
import com.market.orders.entity.OrdersStatus;
import com.market.orders.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.market.core.code.error.OrdersErrorCode.NOT_FOUND_ORDERS_ID;

@Service
@RequiredArgsConstructor
public class OrdersUpdateService {

    private final OrdersRepository ordersRepository;

    @Transactional
    public void updateOrdersStatus(Long ordersId, OrdersStatus ordersStatus) {

        Orders orders = ordersRepository.findById(ordersId).orElseThrow(() -> new OrdersException(NOT_FOUND_ORDERS_ID));
        orders.updateOrdersStatus(ordersStatus);
    }
}

package com.market.orders.service;


import com.market.core.exception.OrdersException;
import com.market.core.exception.ProductException;
import com.market.orders.entity.Orders;
import com.market.orders.entity.OrdersProduct;
import com.market.orders.entity.OrdersStatus;
import com.market.orders.repository.OrdersProductRepository;
import com.market.orders.repository.OrdersRepository;
import com.market.product.entity.Product;
import com.market.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.market.core.code.error.OrdersErrorCode.NOT_FOUND_ORDERS_ID;
import static com.market.core.code.error.ProductErrorCode.NOT_FOUND_PRODUCT_ID;

@Service
@RequiredArgsConstructor
public class OrdersUpdateService {

    private final OrdersRepository ordersRepository;
    private final ProductRepository productRepository;
    private final OrdersProductRepository ordersProductRepository;

    @Transactional
    public void updateOrdersStatus(String ordersId, OrdersStatus ordersStatus) {

        Orders orders = ordersRepository.findById(ordersId).orElseThrow(() -> new OrdersException(NOT_FOUND_ORDERS_ID));

        /**
         * 토스 페이먼츠 연동 후, 수정
         */
        if (ordersStatus == OrdersStatus.CANCELED) {
            List<OrdersProduct> ordersProducts = ordersProductRepository.findAllByOrdersId(orders.getId());

            for (OrdersProduct ordersProduct : ordersProducts) {
                Product product = productRepository.findByProductIdWithPessimisticWrite(ordersProduct.getProduct().getId())
                        .orElseThrow(() -> new ProductException(NOT_FOUND_PRODUCT_ID));

                product.updateProductStock(ordersProduct.getCount());
            }
        }

        orders.updateOrdersStatus(ordersStatus);
    }
}

package com.market.orders.repository;

import com.market.orders.entity.OrdersProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersProductRepository extends JpaRepository<OrdersProduct, Long> {
}

package com.market.orders.repository;

import com.market.orders.entity.OrdersProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrdersProductRepository extends JpaRepository<OrdersProduct, Long> {

    @Query("select op from OrdersProduct op where op.orders.id = :ordersId")
    List<OrdersProduct> findAllByOrdersId(@Param("ordersId") String ordersId);
}

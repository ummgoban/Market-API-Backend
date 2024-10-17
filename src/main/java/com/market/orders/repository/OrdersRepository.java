package com.market.orders.repository;

import com.market.orders.dto.server.OrdersProductsDto;
import com.market.orders.entity.Orders;
import com.market.orders.entity.OrdersStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrdersRepository extends JpaRepository<Orders, Long> {

    @Query("select o from Orders o where o.market.id = :marketId and o.ordersStatus in :ordersStatus")
    List<Orders> getMarketOrdersByMarketIdAndOrdersStatus(@Param("marketId") Long marketId,
                                                                 @Param("ordersStatus") List<OrdersStatus> ordersStatus);

    @Query("select new com.market.orders.dto.server.OrdersProductsDto(" +
            "p.id," +
            "p.productImage," +
            "p.name," +
            "p.originPrice," +
            "p.discountPrice," +
            "p.discountRate," +
            "op.count) " +
            "from OrdersProduct op inner join Product p on op.product.id = p.id " +
            "where op.orders.id = :ordersId")
    List<OrdersProductsDto> getOrdersProductsDtoByOrdersId(@Param("ordersId") Long ordersId);

    @Query("select o from Orders o join fetch o.member")
    Optional<Orders> getOrdersByOrdersId(@Param("ordersId") Long ordersId);
}

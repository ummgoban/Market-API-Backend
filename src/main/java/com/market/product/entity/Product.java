package com.market.product.entity;


import com.market.market.entity.Market;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Product {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "market_id")
    private Market market;

    @Column(name = "product_image")
    private String productImage;

    @Column(name = "name")
    private String name;

    @Column(name = "origin_price")
    private Integer originPrice;

    @Column(name = "discount_price")
    private Integer discountPrice;

    @Column(name = "discount_rate")
    private Integer discountRate;

    @Column(name = "stock")
    private Integer stock;
}

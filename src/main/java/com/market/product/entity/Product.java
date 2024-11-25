package com.market.product.entity;


import com.market.market.entity.Market;
import com.market.product.dto.request.ProductUpdateRequest;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "market_id")
    private Market market;

    @Column(name = "product_status")
    private ProductStatus productStatus;

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

    /**
     * 상품 업데이트
     */
    public void updateProduct(ProductUpdateRequest productUpdateRequest) {
        this.productStatus = productUpdateRequest.getProductStatus();
        this.productImage = productUpdateRequest.getProductImage();
        this.name = productUpdateRequest.getName();
        this.originPrice = productUpdateRequest.getOriginPrice();
        this.discountPrice = productUpdateRequest.getDiscountPrice();
        this.discountRate = productUpdateRequest.getDiscountRate();
        this.stock = productUpdateRequest.getStock();
    }

    public void updateProductStock(Integer count) {
        this.stock += count;
    }
}

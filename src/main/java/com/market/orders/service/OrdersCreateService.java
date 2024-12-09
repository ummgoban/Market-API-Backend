package com.market.orders.service;

import com.market.bucket.dto.server.BucketProductDto;
import com.market.bucket.repository.BucketRepository;
import com.market.core.exception.MarketException;
import com.market.core.exception.MemberException;
import com.market.core.exception.OrdersException;
import com.market.core.exception.ProductException;
import com.market.market.entity.Market;
import com.market.market.repository.MarketRepository;
import com.market.member.entity.Member;
import com.market.member.repository.MemberRepository;
import com.market.orders.dto.request.OrdersCreateRequestDto;
import com.market.orders.dto.response.OrdersCreateResponseDto;
import com.market.orders.entity.*;
import com.market.orders.repository.OrdersProductRepository;
import com.market.orders.repository.OrdersRepository;
import com.market.orders.repository.PaymentRepository;
import com.market.product.entity.Product;
import com.market.product.repository.ProductRepository;
import com.market.utils.random.OrdersIdUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.market.core.code.error.MarketErrorCode.NOT_FOUND_MARKET_ID;
import static com.market.core.code.error.MemberErrorCode.NOT_FOUND_MEMBER_ID;
import static com.market.core.code.error.OrdersErrorCode.BUCKET_ITEM_NOT_FOUND;
import static com.market.core.code.error.OrdersErrorCode.ORDERS_CREATE_METHOD_NOT_ALLOWED;
import static com.market.core.code.error.ProductErrorCode.NOT_FOUND_PRODUCT_ID;


@Service
@RequiredArgsConstructor
public class OrdersCreateService {

    private final MemberRepository memberRepository;
    private final BucketRepository bucketRepository;
    private final MarketRepository marketRepository;
    private final ProductRepository productRepository;
    private final OrdersProductRepository ordersProductRepository;
    private final OrdersRepository ordersRepository;
    private final PaymentRepository paymentRepository;

    private final OrdersIdUtils ordersIdUtils;

    @Transactional
    public OrdersCreateResponseDto createOrders(Long memberId, OrdersCreateRequestDto ordersCreateRequestDto) {

        List<BucketProductDto> bucketProducts = bucketRepository.findAllBucketProductByMemberId(memberId);

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberException(NOT_FOUND_MEMBER_ID));
        Market market = marketRepository.findMarketByProductId(bucketProducts.get(0).getId())
                .orElseThrow(() -> new MarketException(NOT_FOUND_MARKET_ID));

        if (bucketProducts.isEmpty()) {
            throw new OrdersException(BUCKET_ITEM_NOT_FOUND);
        }

        int totalPrice = 0;

        // 상품 재고와 장바구니 갯수 비교
        for (BucketProductDto bucketProductDto : bucketProducts) {
            if (bucketProductDto.getStock() < bucketProductDto.getCount()) {
                throw new OrdersException(ORDERS_CREATE_METHOD_NOT_ALLOWED);
            }

            totalPrice += bucketProductDto.getDiscountPrice() * bucketProductDto.getCount();
        }

        String ordersName = bucketProducts.size() > 1 ?
                bucketProducts.get(0).getName() + " 외 " + (bucketProducts.size() - 1) : bucketProducts.get(0).getName();

        // 주문 생성
        Orders orders = Orders.builder()
                .id(ordersIdUtils.generateOrdersId())
                .member(member)
                .market(market)
                .createdAt(LocalDateTime.now())
                .pickupReservedAt(ordersCreateRequestDto.getPickupReservedAt())
                .ordersPrice(totalPrice)
                .doneAt(null)
//                .ordersStatus(OrdersStatus.IN_PROGRESS)
                .ordersStatus(OrdersStatus.ORDERED)
                .customerRequest(ordersCreateRequestDto.getCustomerRequest())
                .ordersName(ordersName)
                .build();

        ordersRepository.save(orders);

        /**
         * 토스 페이먼츠 연동 후, 삭제할 부분
         */
        paymentRepository.save(Payment.builder()
                .id(ordersIdUtils.generateOrdersId())
                .orders(orders)
                .member(member)
                .approvedAt(LocalDateTime.now())
                .totalAmount(totalPrice)
                .method(PaymentMethod.PICKUP)
                .build());

        // 주문 상품 생성
        for (BucketProductDto bucketProductDto : bucketProducts) {

            Product product = productRepository.findById(bucketProductDto.getId())
                    .orElseThrow(() -> new ProductException(NOT_FOUND_PRODUCT_ID));

            /**
             * 토스 페이먼츠 연동 후, 삭제할 부분
             */
            product.updateProductStock(-bucketProductDto.getCount());

            ordersProductRepository.save(OrdersProduct.builder()
                    .orders(orders)
                    .product(product)
                    .count(bucketProductDto.getCount())
                    .build());
        }

        /**
         * 토스 페이먼츠 연동 후, 삭제할 부분
         */
        bucketRepository.deleteByMemberId(memberId);

        return OrdersCreateResponseDto.builder()
                .ordersId(orders.getId())
                .ordersName(orders.getOrdersName())
                .amount(orders.getOrdersPrice())
                .build();
    }
}

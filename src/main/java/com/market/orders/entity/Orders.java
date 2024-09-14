package com.market.orders.entity;

import com.market.market.entity.Market;
import com.market.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "market_id")
    private Market market;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "pickup_reserved_at")
    private LocalDateTime pickupReservedAt;

    @Column(name = "done_at")
    private LocalDateTime doneAt;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;
}

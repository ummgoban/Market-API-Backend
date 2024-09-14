package com.market.market.entity;

import com.market.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Market {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "open_at")
    private String openAt;

    @Column(name = "close_at")
    private String closeAt;

    @Column(name = "pickup_start_at")
    private String pickupStartAt;

    @Column(name = "pickup_end_at")
    private String pickupEndAt;

}

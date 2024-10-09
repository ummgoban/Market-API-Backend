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

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "market_name")
    private String marketName;

    @Column(name = "summary")
    private String summary;

    @Column(name = "business_number")
    private String businessNumber;

    @Column(name = "address")
    private String address;

    @Column(name = "specific_address")
    private String specificAddress;

    @Column(name = "contact_number")
    private String contactNumber;

    @Column(name = "open_at")
    private String openAt;

    @Column(name = "close_at")
    private String closeAt;

    @Column(name = "pickup_start_at")
    private String pickupStartAt;

    @Column(name = "pickup_end_at")
    private String pickupEndAt;

    public void updateSummary(String summary) {
        this.summary = summary;
    }

    public void updateBusinessHours(String openAt, String closeAt) {
        this.openAt = openAt;
        this.closeAt = closeAt;
    }

    public void updatePickUpHours(String pickupStartAt, String pickupEndAt) {
        this.pickupStartAt = pickupStartAt;
        this.pickupEndAt = pickupEndAt;
    }
}
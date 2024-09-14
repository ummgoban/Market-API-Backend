package com.market.market.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BusinessInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "market_id")
    private Market market;

    @Column(name = "market_name")
    private String marketName;

    @Column(name = "number")
    private String number;

    @Column(name = "address")
    private String address;

    @Column(name = "specific_address")
    private String specificAddress;

    @Column(name = "contact_number")
    private String contactNumber;
}

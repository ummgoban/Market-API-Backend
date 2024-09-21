package com.market.market.dto.server;

import lombok.Builder;
import lombok.Getter;

/**
 * 가게 페이지 네이션 쿼리의 Subjection DTO 입니다.
 */
@Getter
@Builder
public class MarketPagingInfoDto {

    private Long id;

    private String marketName;

    private String address;

    private String specificAddress;

    private String openAt;

    private String closeAt;

    private String pickupStartAt;

    private String pickupEndAt;

}

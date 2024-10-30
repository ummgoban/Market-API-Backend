package com.market.market.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class MarketListResponse {

    private final Long marketId;
    private final String name;
}
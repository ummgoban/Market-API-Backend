package com.market.market.repository;

import com.market.market.dto.server.MarketPagingInfoDto;
import org.springframework.data.domain.Slice;

/**
 * 가게 목록을 커서 기반 페이지네이션을 사용해 조회하는 Repository 인터페이스입니다.
 */
public interface MarketPagingRepository {

    Slice<MarketPagingInfoDto> findMarketByCursorId(Long cursorId, Integer size, Double userLatitude, Double userLongitude);

    Slice<MarketPagingInfoDto> findMemberLikeMarketByCursorId(Long memberId, Long cursorId, Integer size);
}

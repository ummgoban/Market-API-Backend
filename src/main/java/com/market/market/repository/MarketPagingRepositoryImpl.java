package com.market.market.repository;

import com.market.market.dto.response.MarketPagingInfoDto;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * 가게 목록을 커서 기반 페이지네이션을 사용해 조회하는 Repository의 구현체입니다.
 */
@Repository
@RequiredArgsConstructor
public class MarketPagingRepositoryImpl implements MarketPagingRepository {

    private final EntityManager em;

    @Override
    public Slice<MarketPagingInfoDto> findMarketByCursorId(Long cursorId, Integer size) {

        List<MarketPagingInfoDto> content =
                // market 엔티티와 businessInfo 엔티티 조인 후, 데이터 조회
                em.createQuery("select " +
                                "new com.market.market.dto.response.MarketPagingInfoDto" +
                                "(m.id," +
                                "b.marketName," +
                                "b.address," +
                                "b.specificAddress," +
                                "m.openAt," +
                                "m.closeAt," +
                                "m.pickupStartAt," +
                                "m.pickupEndAt) " +
                                "from Market m " +
                                "inner join BusinessInfo b on m.id = b.market.id " +
                                "where (:cursorId = 0L or m.id > :cursorId) " +
                                "order by m.id asc ", MarketPagingInfoDto.class)
                        .setParameter("cursorId", cursorId)
                        .setMaxResults(size + 1)
                        .getResultList();

        boolean hasNext = false;

        // 다음 페이지가 있는 경우,
        if (content.size() > size) {
            hasNext = true;
            return new SliceImpl<>(new ArrayList<>(content.subList(0, size)), Pageable.ofSize(size), hasNext);
        }

        return new SliceImpl<>(content, Pageable.ofSize(size), hasNext);
    }
}

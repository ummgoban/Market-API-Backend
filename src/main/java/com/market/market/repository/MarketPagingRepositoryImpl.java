package com.market.market.repository;

import com.market.market.dto.server.MarketPagingInfoDto;
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
                                "new com.market.market.dto.server.MarketPagingInfoDto" +
                                "(m.id," +
                                "m.marketName," +
                                "m.address," +
                                "m.specificAddress," +
                                "m.openAt," +
                                "m.closeAt," +
                                "m.pickupStartAt," +
                                "m.pickupEndAt) " +
                                "from Market m " +
                                "where (:cursorId = 0L or m.id > :cursorId) " +
                                "order by m.id asc ", MarketPagingInfoDto.class)
                        .setParameter("cursorId", cursorId)
                        .setMaxResults(size + 1)
                        .getResultList();

        return hasNext(content, size);
    }

    @Override
    public Slice<MarketPagingInfoDto> findMemberLikeMarketByCursorId(Long memberId, Long cursorId, Integer size) {

        List<MarketPagingInfoDto> content =
                // market 엔티티와 businessInfo 엔티티 조인 후, 데이터 조회
                em.createQuery("select " +
                                "new com.market.market.dto.server.MarketPagingInfoDto" +
                                "(m.id," +
                                "m.marketName," +
                                "m.address," +
                                "m.specificAddress," +
                                "m.openAt," +
                                "m.closeAt," +
                                "m.pickupStartAt," +
                                "m.pickupEndAt) " +
                                "from Market m " +
                                "where (:cursorId = 0L or m.id > :cursorId) " +
                                "and exists (" +
                                "               select 1 " +
                                "               from MarketLike ml " +
                                "               where ml.market.id = m.id and ml.member.id = :memberId " +
                                "           ) " +
                                "order by m.id asc ", MarketPagingInfoDto.class)
                        .setParameter("cursorId", cursorId)
                        .setParameter("memberId", memberId)
                        .setMaxResults(size + 1)
                        .getResultList();

        return hasNext(content, size);
    }

    private Slice<MarketPagingInfoDto> hasNext(List<MarketPagingInfoDto> content, Integer size) {

        boolean hasNext = false;

        // 다음 페이지가 있는 경우,
        if (content.size() > size) {
            hasNext = true;
            return new SliceImpl<>(new ArrayList<>(content.subList(0, size)), Pageable.ofSize(size), hasNext);
        }

        return new SliceImpl<>(content, Pageable.ofSize(size), hasNext);
    }
}

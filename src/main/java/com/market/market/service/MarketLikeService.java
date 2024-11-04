package com.market.market.service;

import com.market.core.exception.MarketException;
import com.market.core.exception.MarketLikeException;
import com.market.core.exception.MemberException;
import com.market.market.entity.Market;
import com.market.market.entity.MarketLike;
import com.market.market.repository.MarketLikeRepository;
import com.market.market.repository.MarketRepository;
import com.market.member.entity.Member;
import com.market.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.market.core.code.error.MarketErrorCode.NOT_FOUND_MARKET_ID;
import static com.market.core.code.error.MarketLikeErrorCode.DUPLICATE_MARKET_LIKE;
import static com.market.core.code.error.MarketLikeErrorCode.MARKET_LIKE_NOT_FOUND;
import static com.market.core.code.error.MemberErrorCode.NOT_FOUND_MEMBER_ID;

/**
 * 가게 찜 관련 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class MarketLikeService {

    private final MemberRepository memberRepository;
    private final MarketRepository marketRepository;
    private final MarketLikeRepository marketLikeRepository;

    @Transactional
    public void createMarketLike(Long memberId, Long marketId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberException(NOT_FOUND_MEMBER_ID));
        Market market = marketRepository.findById(marketId).orElseThrow(() -> new MarketException(NOT_FOUND_MARKET_ID));

        boolean isExists = marketLikeRepository.existsByMemberAndMarket(member, market);
        if (isExists) {
            throw new MarketLikeException(DUPLICATE_MARKET_LIKE);
        }

        marketLikeRepository.save(MarketLike.builder()
                .member(member)
                .market(market)
                .build());
    }

    @Transactional
    public void deleteMarketLike(Long memberId, Long marketId) {
        MarketLike marketLike = marketLikeRepository.findMarketLikeByMemberAndMarket(memberId, marketId).orElseThrow(()
                -> new MarketLikeException(MARKET_LIKE_NOT_FOUND));

        marketLikeRepository.delete(marketLike);
    }

}

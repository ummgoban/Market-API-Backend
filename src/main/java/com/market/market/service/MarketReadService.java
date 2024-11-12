package com.market.market.service;

import com.market.core.code.error.MarketErrorCode;
import com.market.core.code.error.MemberErrorCode;
import com.market.core.exception.MarketException;
import com.market.core.exception.MemberException;
import com.market.market.dto.response.*;
import com.market.market.dto.server.BusinessValidateResponseDto;
import com.market.market.entity.BusinessStatus;
import com.market.market.entity.Market;
import com.market.market.entity.MarketImage;
import com.market.market.entity.Tag;
import com.market.market.entity.ValidStatus;
import com.market.market.repository.MarketImageRepository;
import com.market.market.repository.MarketLikeRepository;
import com.market.market.repository.MarketRepository;
import com.market.member.entity.Member;
import com.market.member.repository.MemberRepository;
import com.market.market.repository.TagRepository;
import com.market.product.dto.response.ProductResponse;
import com.market.product.entity.Product;
import com.market.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 가게 Read 관련 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class MarketReadService {

    private final MemberRepository memberRepository;
    private final MarketRepository marketRepository;
    private final MarketImageRepository marketImageRepository;
    private final MarketLikeRepository marketLikeRepository;
    private final ProductRepository productRepository;
    private final TagRepository tagRepository;
    private final BusinessValidateService businessValidateService;

    /**
     * 가게 상세 조회 트랜잭션입니다.
     */
    @Transactional(readOnly = true)
    public MarketSpecificResponse getSpecificMarket(Long memberId, Long marketId) {

        boolean hasLike = false;

        Market market = marketRepository.findById(marketId)
                .orElseThrow(() -> new MarketException(MarketErrorCode.NOT_FOUND_MARKET_ID));

        if (memberId != null) {
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER_ID));

            hasLike = marketLikeRepository.existsByMemberAndMarket(member, market);
        }

        // 가게 이미지들 조회
        List<MarketImage> marketImages = marketImageRepository.findAllByMarketId(marketId);

        // 가게 상품들 조회
        List<Product> products = productRepository.findAllByMarketId(marketId);

        // 가게의 상품 정보를 담을 배열 선언
        List<ProductResponse> productResponses = new ArrayList<>();

        // 상품 각각에 대해 태그 정보 조회 후, productResponse 객체 생성 후 productResponses에 저장
        for (Product product : products) {
            List<Tag> tags = tagRepository.findAllByProductId(product.getId());

            ProductResponse productResponse = ProductResponse.from(product, tags);
            productResponses.add(productResponse);
        }

        return MarketSpecificResponse.from(market, marketImages, hasLike, productResponses);
    }

    /**
     * 사장님의 가게 목록을 조회합니다.
     */
    public List<MarketListResponse> getMarketList(Long memberId) {
        // 회원 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER_ID));

        // 마켓 리스트 조회
        List<Market> marketList = marketRepository.findAllByMemberId(member.getId());

        return marketList.stream()
                .map(market -> MarketListResponse.builder()
                        .marketId(market.getId())
                        .name(market.getMarketName())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * 사업자 등록 번호 유효성 검증
     */
    public BusinessNumberValidateResponse validateBusinessValidate(String businessNumber, String startDate, String name, String marketName) {
        BusinessValidateResponseDto businessValidateResponseDto = businessValidateService.getBusinessStatus(businessNumber, startDate, name, marketName);

        return BusinessNumberValidateResponse.builder()
                .validBusinessNumber(isValidBusinessNumber(businessValidateResponseDto))
                .businessNumberValidateInfoResponse(BusinessNumberValidateInfoResponse.builder()
                        .businessNumber(businessValidateResponseDto.getData().get(0).getBusinessNumber())
                        .startDate(businessValidateResponseDto.getData().get(0).getRequestParam().getStartDate())
                        .memberName(businessValidateResponseDto.getData().get(0).getRequestParam().getPrimaryName())
                        .marketName(businessValidateResponseDto.getData().get(0).getRequestParam().getMarketName())
                        .build())
                .build();
    }

    /**
     * 사업자 등록 번호가 유효한지 여부 확인
     */
    public boolean isValidBusinessNumber(BusinessValidateResponseDto businessValidateResponseDto) {
        // 유효성 확인 (01: 유효, 02: 유효 X)
        ValidStatus validStatus = businessValidateResponseDto.getData().get(0).getValidStatus();
        if (ValidStatus.INVALID.equals(validStatus)) {
            return false;
        }

        String startDate = businessValidateResponseDto.getData().get(0).getRequestParam().getStartDate();
        // TODO: 이름 비교 소셜 로그인 검수 이후 추가
        String name = businessValidateResponseDto.getData().get(0).getRequestParam().getPrimaryName();
        BusinessStatus businessStatus = businessValidateResponseDto.getData().get(0).getStatus().getBusinessStatus();
        String taxType = businessValidateResponseDto.getData().get(0).getStatus().getTaxType();

        // 개업일자 LocalDate 타입으로 변환
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate inputDate = LocalDate.parse(startDate, formatter);

        return !(BusinessStatus.SUSPENDED.equals(businessStatus) || // 휴업자일 경우
                BusinessStatus.CLOSED.equals(businessStatus) || // 폐업자일 경우
                inputDate.isAfter(LocalDate.now()) || // 개업일이 현재 시간보다 미래일 경우
                "국세청에 등록되지 않은 사업자등록번호입니다.".equals(taxType)); // 등록되지 않은 사업자 등록 번호일 경우
    }
}
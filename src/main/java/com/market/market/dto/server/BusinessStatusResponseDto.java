package com.market.market.dto.server;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

/**
 * 외부 API로부터 사업자 등록 번호 조회 결과를 응답받는 DTO 클래스입니다.
 */
@Getter
public class BusinessStatusResponseDto {

    @JsonProperty("data")
    private List<Data> data;

    @Getter
    public static class Data {

        @JsonProperty("b_no")
        private String businessNumber;

        @JsonProperty("tax_type")
        private String taxType;
    }
}
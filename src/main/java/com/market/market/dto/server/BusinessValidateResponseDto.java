package com.market.market.dto.server;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

/**
 * 외부 API로부터 사업자 등록 번호 조회 결과를 응답받는 DTO 클래스입니다.
 */
@Getter
public class BusinessValidateResponseDto {

    @JsonProperty("data")
    private List<Data> data;

    @Getter
    public static class Data {

        @JsonProperty("b_no")
        private String businessNumber; // 사업자 등록 번호

        @JsonProperty("valid")
        private String valid; // 유효 여부

        @JsonProperty("request_param")
        private RequestParam requestParam;

        @JsonProperty("status")
        private Status status;

        @Getter
        public static class RequestParam {
            @JsonProperty("start_dt")
            private String startDate; // 개업일자

            @JsonProperty("p_nm")
            private String primaryName; // 이름
        }

        @Getter
        public static class Status {

            @JsonProperty("b_stt")
            private String businessStatus; // 계속 사업자, 휴업자, 폐업자

            @JsonProperty("tax_type")
            private String taxType; // 세금 타입
        }
    }
}
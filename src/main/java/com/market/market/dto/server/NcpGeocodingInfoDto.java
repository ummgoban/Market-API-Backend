package com.market.market.dto.server;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

/**
 * Geocoding 정보 DTO 클래스입니다.
 */
@Getter
public class NcpGeocodingInfoDto {

    @JsonProperty("status")
    private String status;

    @JsonProperty("meta")
    private Object meta;

    @JsonProperty("meta.totalCount")
    private Integer metaTotalCount;

    @JsonProperty("meta.page")
    private Integer metaPage;

    @JsonProperty("meta.count")
    private Integer metaCount;

    @JsonProperty("addresses")
    List<Address> addresses;

    @JsonProperty("errorMessage")
    private String errorMessage;

    @Getter
    public static class Address {

        @JsonProperty("roadAddress")
        private String roadAddress;

        @JsonProperty("jibunAddress")
        private String jibunAddress;

        @JsonProperty("englishAddress")
        private String englishAddress;

        @JsonProperty("addressElements")
        List<AddressElement> addressElements;

        @JsonProperty("x")
        private String x;

        @JsonProperty("y")
        private String y;

        @JsonProperty("distance")
        private Double distance;

        @Getter
        public static class AddressElement {

            @JsonProperty("type")
            private List<String> type;

            @JsonProperty("longName")
            private String longName;

            @JsonProperty("shortName")
            private String shortName;

            @JsonProperty("code")
            private String code;
        }
    }
}

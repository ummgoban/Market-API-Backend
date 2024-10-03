package com.market.market.dto.server;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BusinessValidateRequestDto {

    private String b_no;
    private String start_dt;
    private String p_nm;
}
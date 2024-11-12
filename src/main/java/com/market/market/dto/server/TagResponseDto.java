package com.market.market.dto.server;


import com.market.market.entity.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

/**
 * 태그 조회 DTO 입니다.
 */
@Getter
@Builder
public class TagResponseDto {

    @Schema(description = "태그 ID 값")
    private Long id;

    @Schema(description = "태그 이름")
    private String tagName;


    public static TagResponseDto from(Tag tag) {
        return TagResponseDto.builder()
                .id(tag.getId())
                .tagName(tag.getName())
                .build();
    }
}

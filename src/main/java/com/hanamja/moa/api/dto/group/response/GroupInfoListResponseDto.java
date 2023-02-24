package com.hanamja.moa.api.dto.group.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(description = "모임 List 응답 DTO")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupInfoListResponseDto {

    @Schema(description = "모임 List")
    List<GroupInfoResponseDto> items;

    public static GroupInfoListResponseDto of (List<GroupInfoResponseDto> items) {
        GroupInfoListResponseDto dto = new GroupInfoListResponseDto();
        dto.items = items;

        return dto;
    }
}

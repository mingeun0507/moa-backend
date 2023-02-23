package com.hanamja.moa.api.dto.group.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupInfoListResponseDto {

    List<GroupInfoResponseDto> items;

    public static GroupInfoListResponseDto of (List<GroupInfoResponseDto> items) {
        GroupInfoListResponseDto dto = new GroupInfoListResponseDto();
        dto.items = items;

        return dto;
    }
}

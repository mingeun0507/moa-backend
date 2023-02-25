package com.hanamja.moa.api.dto.group.response;

import com.hanamja.moa.api.entity.group.State;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupStateInfoResponseDto {
    private List<Data> info;

    @Builder
    public static class Data {
        private State state;
        private List<GroupInfoResponseDto> groups;
    }

    @Builder
    public GroupStateInfoResponseDto(List<Data> info) {
        this.info = info;
    }

    public static GroupStateInfoResponseDto of (List<Data> data) {
        GroupStateInfoResponseDto dto = new GroupStateInfoResponseDto();
        dto.info = data;

        return dto;
    }
}

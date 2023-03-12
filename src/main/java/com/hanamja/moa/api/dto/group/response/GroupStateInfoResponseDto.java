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
    private State state;
    private List<GroupInfoResponseDto> groups;

    @Builder
    public GroupStateInfoResponseDto(State state, List<GroupInfoResponseDto> groups) {
        this.state = state;
        this.groups = groups;
    }

    public static GroupStateInfoResponseDto of (State state, List<GroupInfoResponseDto> groups) {
        return GroupStateInfoResponseDto.builder()
                .state(state).groups(groups).build();
    }
}

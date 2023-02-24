package com.hanamja.moa.api.dto.group.response;

import com.hanamja.moa.api.entity.group.Group;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupDetailInfoResponseDto {
    private GroupInfoResponseDto groupInfoResponseDto;
    private List<SimpleUserInfoResponseDto> userInfoDtoList;
    private int point;

    @Builder
    public GroupDetailInfoResponseDto(GroupInfoResponseDto groupInfoResponseDto, List<SimpleUserInfoResponseDto> userInfoDtoList, int point) {
        this.groupInfoResponseDto = groupInfoResponseDto;
        this.userInfoDtoList = userInfoDtoList;
        this.point = point;
    }

    public static GroupDetailInfoResponseDto from(Group group, List<String> hashtags, List<SimpleUserInfoResponseDto> userInfoDtoList, int point) {
        return GroupDetailInfoResponseDto
                .builder()
                .groupInfoResponseDto(GroupInfoResponseDto.from(group, hashtags))
                .userInfoDtoList(userInfoDtoList)
                .point(point)
                .build();
    }

}

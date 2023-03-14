package com.hanamja.moa.api.dto.group.response;

import com.hanamja.moa.api.dto.comment.response.CommentInfoResponseDto;
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
    private CommentInfoResponseDto commentInfoResponseDto;

    @Builder
    public GroupDetailInfoResponseDto(GroupInfoResponseDto groupInfoResponseDto, List<SimpleUserInfoResponseDto> userInfoDtoList, int point, CommentInfoResponseDto commentInfoResponseDto) {
        this.groupInfoResponseDto = groupInfoResponseDto;
        this.userInfoDtoList = userInfoDtoList;
        this.point = point;
        this.commentInfoResponseDto = commentInfoResponseDto;
    }

    public static GroupDetailInfoResponseDto from(Group group, List<String> hashtags, List<SimpleUserInfoResponseDto> userInfoDtoList, int point, CommentInfoResponseDto commentInfoResponseDto) {
        return GroupDetailInfoResponseDto
                .builder()
                .groupInfoResponseDto(GroupInfoResponseDto.from(group, hashtags))
                .userInfoDtoList(userInfoDtoList)
                .point(point)
                .commentInfoResponseDto(commentInfoResponseDto)
                .build();
    }

    public static GroupDetailInfoResponseDto from(Group group, List<String> hashtags, List<SimpleUserInfoResponseDto> userInfoDtoList, CommentInfoResponseDto commentInfoResponseDto) {
        return GroupDetailInfoResponseDto
                .builder()
                .groupInfoResponseDto(GroupInfoResponseDto.from(group, hashtags))
                .userInfoDtoList(userInfoDtoList)
                .commentInfoResponseDto(commentInfoResponseDto)
                .build();
    }
}

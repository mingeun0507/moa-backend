package com.hanamja.moa.api.dto.group.response;

import com.hanamja.moa.api.entity.group.Group;
import com.hanamja.moa.api.entity.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupDetailInfoResponseDto {
    private GroupInfoResponseDto groupInfoResponseDto;
    private List<SimpleUserInfoDto> userInfoDtoList;
    private int point;

    @Builder
    public GroupDetailInfoResponseDto(GroupInfoResponseDto groupInfoResponseDto, List<SimpleUserInfoDto> userInfoDtoList, int point) {
        this.groupInfoResponseDto = groupInfoResponseDto;
        this.userInfoDtoList = userInfoDtoList;
        this.point = point;
    }

    public static GroupDetailInfoResponseDto from(Group group, List<String> hashtags, List<SimpleUserInfoDto> userInfoDtoList, int point) {
        return GroupDetailInfoResponseDto
                .builder()
                .groupInfoResponseDto(GroupInfoResponseDto.from(group, hashtags))
                .userInfoDtoList(userInfoDtoList)
                .point(point)
                .build();
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SimpleUserInfoDto {
        private Long id;
        private String name;
        private String imageLink;

        @Builder
        public SimpleUserInfoDto(Long id, String name, String imageLink) {
            this.id = id;
            this.name = name;
            this.imageLink = imageLink;
        }

        public static SimpleUserInfoDto from(User user) {
            return SimpleUserInfoDto
                    .builder()
                    .id(user.getId())
                    .name(user.getName())
                    .imageLink(user.getImageLink())
                    .build();
        }
    }
}

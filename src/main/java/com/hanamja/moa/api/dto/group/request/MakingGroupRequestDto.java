package com.hanamja.moa.api.dto.group.request;

import com.hanamja.moa.api.entity.group.Group;
import com.hanamja.moa.api.entity.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MakingGroupRequestDto {

    private String name;
    private String description;
    private Long maxPeopleNum;
    private LocalDateTime meetingAt;
    private String hashtags;

    public static Group toEntity(MakingGroupRequestDto makingGroupRequestDto, User user) {
        return Group
                .builder()
                .name(makingGroupRequestDto.getName())
                .description(makingGroupRequestDto.getDescription())
                .maxPeopleNum(makingGroupRequestDto.getMaxPeopleNum())
                .currentPeopleNum(1L)
                .meetingAt(makingGroupRequestDto.getMeetingAt())
                .maker(user)
                .build();
    }
}

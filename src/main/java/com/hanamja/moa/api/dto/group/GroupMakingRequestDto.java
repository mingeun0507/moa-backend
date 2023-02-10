package com.hanamja.moa.api.dto.group;

import com.hanamja.moa.api.entity.group.Group;
import com.hanamja.moa.api.entity.user.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GroupMakingRequestDto {

    private String name;
    private String description;
    private Long maxPeopleNum;
    private LocalDateTime meetingAt;
    private String hashtags;

    public static Group toEntity(GroupMakingRequestDto groupMakingRequestDto, User user) {
        return Group
                .builder()
                .name(groupMakingRequestDto.getName())
                .description(groupMakingRequestDto.getDescription())
                .maxPeopleNum(groupMakingRequestDto.getMaxPeopleNum())
                .currentPeopleNum(1L)
                .meetingAt(groupMakingRequestDto.getMeetingAt())
                .maker(user)
                .build();
    }
}

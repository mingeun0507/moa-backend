package com.hanamja.moa.api.dto.group.response;

import com.hanamja.moa.api.entity.group.Group;
import com.hanamja.moa.api.entity.group.State;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GroupInfoResponseDto {
    private Long id;
    private String name;
    private String description;
    private State state;
    private Long maxPeopleNum;
    private Long currentPeopleNum;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime meetingAt;
    private String imageLink;

    @Builder
    public GroupInfoResponseDto(Long id, String name, String description, State state, Long maxPeopleNum, Long currentPeopleNum, LocalDateTime createdAt, LocalDateTime modifiedAt, LocalDateTime meetingAt, String imageLink) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.state = state;
        this.maxPeopleNum = maxPeopleNum;
        this.currentPeopleNum = currentPeopleNum;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.meetingAt = meetingAt;
        this.imageLink = imageLink;
    }

    public static GroupInfoResponseDto from(Group group) {
        return GroupInfoResponseDto
                .builder()
                .id(group.getId())
                .name(group.getName())
                .description(group.getDescription())
                .state(group.getState())
                .maxPeopleNum(group.getMaxPeopleNum())
                .currentPeopleNum(group.getCurrentPeopleNum())
                .createdAt(group.getCreatedAt())
                .modifiedAt(group.getModifiedAt())
                .meetingAt(group.getMeetingAt())
                .imageLink(group.getImageLink())
                .build();
    }

}

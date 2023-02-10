package com.hanamja.moa.api.dto.group;

import com.hanamja.moa.api.entity.group.Group;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GroupInfoResponseDto {
    private Long id;
    private String name;
    private String description;
    private Long maxPeopleNum;
    private Long currentPeopleNum;
    private LocalDateTime createdAt;
    private LocalDateTime meetingAt;
    private String imageLink;

    @Builder
    public GroupInfoResponseDto(Long id, String name, String description, Long maxPeopleNum, Long currentPeopleNum, LocalDateTime createdAt, LocalDateTime meetingAt, String imageLink) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.maxPeopleNum = maxPeopleNum;
        this.currentPeopleNum = currentPeopleNum;
        this.createdAt = createdAt;
        this.meetingAt = meetingAt;
        this.imageLink = imageLink;
    }

    public static GroupInfoResponseDto from(Group group) {
        return GroupInfoResponseDto
                .builder()
                .id(group.getId())
                .name(group.getName())
                .description(group.getDescription())
                .maxPeopleNum(group.getMaxPeopleNum())
                .currentPeopleNum(group.getCurrentPeopleNum())
                .createdAt(group.getCreatedAt())
                .meetingAt(group.getMeetingAt())
                .imageLink(group.getImageLink())
                .build();
    }

}

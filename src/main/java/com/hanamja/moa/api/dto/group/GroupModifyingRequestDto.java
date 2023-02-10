package com.hanamja.moa.api.dto.group;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GroupModifyingRequestDto {
    private Long id;
    private String name;
    private String description;
    private Long maxPeopleNum;
    private LocalDateTime meetingAt;
    private String hashtags;
}

package com.hanamja.moa.api.dto.group.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ModifyingGroupRequestDto {
    private Long id;
    private String name;
    private String description;
    private Long maxPeopleNum;
    private LocalDateTime meetingAt;
    private String hashtags;
}

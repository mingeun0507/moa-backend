package com.hanamja.moa.api.dto.group.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ModifyingGroupRequestDto {

    @NotNull
    private Long id;

    private String name;

    private String description;

    private Long maxPeopleNum;

    private LocalDateTime meetingAt;

    private String hashtags;
}

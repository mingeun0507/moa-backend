package com.hanamja.moa.api.dto.group.request;

import com.hanamja.moa.api.entity.group.Group;
import com.hanamja.moa.api.entity.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MakingGroupRequestDto {

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private Long maxPeopleNum;

    private LocalDateTime meetingAt;

    private String hashtags;

    public static Group toEntity(MakingGroupRequestDto makingGroupRequestDto, User user) {
        LocalDateTime meetingAt = makingGroupRequestDto.getMeetingAt();
        if (Optional.ofNullable(makingGroupRequestDto.getMeetingAt()).isPresent()){
            meetingAt = ZonedDateTime.of(makingGroupRequestDto.getMeetingAt(), ZoneId.of("Asia/Seoul"))
                    .toLocalDateTime().plusHours(9L);
        }
        return Group
                .builder()
                .name(makingGroupRequestDto.getName())
                .description(makingGroupRequestDto.getDescription())
                .maxPeopleNum(makingGroupRequestDto.getMaxPeopleNum())
                .currentPeopleNum(1L)
                .meetingAt(meetingAt)
                .maker(user)
                .build();
    }
}

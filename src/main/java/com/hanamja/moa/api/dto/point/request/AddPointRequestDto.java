package com.hanamja.moa.api.dto.point.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AddPointRequestDto {

    @Positive
    private Long userId;

    @NotEmpty
    private String title;

    @Positive
    private Long point;
}

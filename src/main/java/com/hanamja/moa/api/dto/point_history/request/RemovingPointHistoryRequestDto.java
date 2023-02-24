package com.hanamja.moa.api.dto.point_history.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RemovingPointHistoryRequestDto {

    @NotNull
    private Long id;
}

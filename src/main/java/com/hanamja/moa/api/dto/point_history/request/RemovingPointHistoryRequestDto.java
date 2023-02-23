package com.hanamja.moa.api.dto.point_history.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RemovingPointHistoryRequestDto {
    private Long id;
}

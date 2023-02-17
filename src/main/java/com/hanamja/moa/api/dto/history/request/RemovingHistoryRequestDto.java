package com.hanamja.moa.api.dto.history.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RemovingHistoryRequestDto {
    private Long id;
}

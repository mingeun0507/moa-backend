package com.hanamja.moa.api.dto.group.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RemovingGroupRequestDto {
    Long id;
}

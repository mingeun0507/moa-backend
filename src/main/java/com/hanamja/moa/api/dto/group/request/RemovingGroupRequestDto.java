package com.hanamja.moa.api.dto.group.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RemovingGroupRequestDto {

    @NotNull
    Long id;
}

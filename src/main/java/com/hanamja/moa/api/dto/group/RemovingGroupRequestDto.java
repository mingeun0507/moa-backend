package com.hanamja.moa.api.dto.group;

import lombok.Builder;
import lombok.Data;

@Data
public class RemovingGroupRequestDto {
    Long id;

    @Builder
    public RemovingGroupRequestDto(Long id) {
        this.id = id;
    }
}

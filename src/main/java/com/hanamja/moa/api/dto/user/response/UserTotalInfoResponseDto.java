package com.hanamja.moa.api.dto.user.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserTotalInfoResponseDto {
    private UserInfoResponseDto info;
    private int rank;

    @Builder
    public UserTotalInfoResponseDto(UserInfoResponseDto info, int rank) {
        this.info = info;
        this.rank = rank;
    }
}

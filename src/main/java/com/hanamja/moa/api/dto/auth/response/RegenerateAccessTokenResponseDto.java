package com.hanamja.moa.api.dto.auth.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RegenerateAccessTokenResponseDto {

    private String accessToken;

    private String refreshToken;

    public static RegenerateAccessTokenResponseDto of(String accessToken, String refreshToken) {
        RegenerateAccessTokenResponseDto responseDto = new RegenerateAccessTokenResponseDto();
        responseDto.accessToken = accessToken;
        responseDto.refreshToken = refreshToken;

        return responseDto;
    }
}

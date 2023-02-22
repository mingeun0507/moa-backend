package com.hanamja.moa.api.dto.auth.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginResponseDto {

    private String accessToken;

    private String refreshToken;

    public static LoginResponseDto of(String accessToken, String refreshToken) {
        LoginResponseDto responseDto = new LoginResponseDto();
        responseDto.accessToken = accessToken;
        responseDto.refreshToken = refreshToken;

        return responseDto;
    }
}

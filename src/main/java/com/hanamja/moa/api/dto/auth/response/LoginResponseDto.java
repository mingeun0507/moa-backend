package com.hanamja.moa.api.dto.auth.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginResponseDto {

    private String accessToken;

    private String refreshToken;

    private boolean isOnBoarded;

    private boolean isActive;


    public static LoginResponseDto of(String accessToken, String refreshToken, boolean isOnBoarded, boolean isActive) {
        LoginResponseDto responseDto = new LoginResponseDto();
        responseDto.accessToken = accessToken;
        responseDto.refreshToken = refreshToken;
        responseDto.isOnBoarded = isOnBoarded;
        responseDto.isActive = isActive;

        return responseDto;
    }
}

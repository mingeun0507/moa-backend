package com.hanamja.moa.api.dto.auth.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginResponseDto {

    private List<String> accessTokens;

    private String refreshToken;

    private boolean isOnBoarded;

    private boolean isActive;


    public static LoginResponseDto of(List<String> accessTokens, String refreshToken, boolean isOnBoarded, boolean isActive) {
        LoginResponseDto responseDto = new LoginResponseDto();
        responseDto.accessTokens = accessTokens;
        responseDto.refreshToken = refreshToken;
        responseDto.isOnBoarded = isOnBoarded;
        responseDto.isActive = isActive;

        return responseDto;
    }
}

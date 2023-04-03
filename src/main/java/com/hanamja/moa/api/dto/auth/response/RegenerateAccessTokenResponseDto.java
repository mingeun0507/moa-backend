package com.hanamja.moa.api.dto.auth.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RegenerateAccessTokenResponseDto {

    private List<String> accessTokens;

    private String refreshToken;

    public static RegenerateAccessTokenResponseDto of(List<String> accessTokens, String refreshToken) {
        RegenerateAccessTokenResponseDto responseDto = new RegenerateAccessTokenResponseDto();
        responseDto.accessTokens = accessTokens;
        responseDto.refreshToken = refreshToken;

        return responseDto;
    }
}

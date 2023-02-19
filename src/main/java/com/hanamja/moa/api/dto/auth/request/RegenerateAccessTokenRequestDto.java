package com.hanamja.moa.api.dto.auth.request;

import lombok.Getter;

@Getter
public class RegenerateAccessTokenRequestDto {

    private String accessToken;

    private String refreshToken;
}

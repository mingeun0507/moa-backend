package com.hanamja.moa.api.dto.auth.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RegenerateAccessTokenRequestDto {

    private String accessToken;

    private String refreshToken;
}

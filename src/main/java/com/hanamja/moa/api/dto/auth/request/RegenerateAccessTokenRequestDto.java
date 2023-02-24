package com.hanamja.moa.api.dto.auth.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class RegenerateAccessTokenRequestDto {

    @NotNull
    private String refreshToken;
}

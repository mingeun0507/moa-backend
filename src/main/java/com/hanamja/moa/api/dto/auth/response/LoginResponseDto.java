package com.hanamja.moa.api.dto.auth.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginResponseDto {

    private List<TokenInfo> accessTokens;

    private String refreshToken;

    private boolean isOnBoarded;

    private boolean isActive;


    public static LoginResponseDto of(List<Map<Long, String>> accessTokens, String refreshToken, boolean isOnBoarded, boolean isActive) {
        LoginResponseDto responseDto = new LoginResponseDto();

        responseDto.accessTokens = accessTokens.stream()
                .map(x -> TokenInfo.builder().tokenInfo(x).build())
                .collect(Collectors.toList());
        responseDto.refreshToken = refreshToken;
        responseDto.isOnBoarded = isOnBoarded;
        responseDto.isActive = isActive;

        return responseDto;
    }

    public static class TokenInfo {
        public Long deptId;
        public String token;

        @Builder
        public TokenInfo(Map<Long, String> tokenInfo) {
            this.deptId = (Long) tokenInfo.keySet().toArray()[0];
            this.token = (String) tokenInfo.values().toArray()[0];
        }
    }
}

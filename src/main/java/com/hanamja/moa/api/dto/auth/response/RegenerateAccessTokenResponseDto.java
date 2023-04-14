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
public class RegenerateAccessTokenResponseDto {

    private List<TokenInfo> accessTokens;

    private String refreshToken;

    public static RegenerateAccessTokenResponseDto of(List<Map<Long, String>> accessTokens, String refreshToken) {
        RegenerateAccessTokenResponseDto responseDto = new RegenerateAccessTokenResponseDto();
        responseDto.accessTokens = accessTokens.stream()
                .map(x -> TokenInfo.builder().tokenInfo(x).build())
                .collect(Collectors.toList());
        responseDto.refreshToken = refreshToken;

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

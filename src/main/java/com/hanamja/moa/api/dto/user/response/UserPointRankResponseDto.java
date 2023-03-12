package com.hanamja.moa.api.dto.user.response;

import com.hanamja.moa.api.entity.user.User;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserPointRankResponseDto {
    private String role;
    private List<RankTab> ranks;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class RankTab{
        private Long userId;
        private Long rank;
        private String username;
        private String img;
        private Long point;

        public static RankTab of (User user, Long rank){
            return RankTab.builder()
                    .userId(user.getId()).rank(rank)
                    .username(user.getName()).img(user.getImageLink())
                    .point(user.getPoint())
                    .build();
        }
    }

    @Builder
    public UserPointRankResponseDto(String role, List<RankTab> ranks) {
        this.role = role;
        this.ranks = ranks;
    }

    public static List<RankTab> makeRanks(List<User> users){
        return users.stream()
                .map(user -> UserPointRankResponseDto.RankTab.of(user, Long.valueOf(users.indexOf(user)+1)))
                .collect(Collectors.toList());
    }

    public static UserPointRankResponseDto of (String role, List<User> users){
        return UserPointRankResponseDto.builder()
                .role(role)
                .ranks(makeRanks(users))
                .build();
    }
}

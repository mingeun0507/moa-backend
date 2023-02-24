package com.hanamja.moa.api.dto.group.response;

import com.hanamja.moa.api.entity.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SimpleUserInfoResponseDto {
    private Long id;
    private String name;
    private String imageLink;

    @Builder
    public SimpleUserInfoResponseDto(Long id, String name, String imageLink) {
        this.id = id;
        this.name = name;
        this.imageLink = imageLink;
    }

    public static SimpleUserInfoResponseDto from(User user) {
        return SimpleUserInfoResponseDto
                .builder()
                .id(user.getId())
                .name(user.getName())
                .imageLink(user.getImageLink())
                .build();
    }
}

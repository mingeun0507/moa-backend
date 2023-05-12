package com.hanamja.moa.api.dto.post.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreatePostImageResponseDto {
    private String imageUrl;

    @Builder
    public CreatePostImageResponseDto(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

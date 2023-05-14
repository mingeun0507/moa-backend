package com.hanamja.moa.api.dto.post.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EditPostRequestDto {
    private Long postId;
    private String title;
    private Long categoryId;
    private String content;
    private List<String> imageUrls;

}

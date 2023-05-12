package com.hanamja.moa.api.dto.post.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardPostSaveAndEditRequestDto {
    private String title;
    private Long categoryId;
    private String content;

}

package com.hanamja.moa.api.dto.post_comment.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreatePostCommentRequestDto {

    private Long parentCommentId;

    @NotNull
    private String content;

    private int commentOrder;
}

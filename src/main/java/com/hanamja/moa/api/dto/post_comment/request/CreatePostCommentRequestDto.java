package com.hanamja.moa.api.dto.post_comment.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CreatePostCommentRequestDto {

    private Long parentCommentId;

    @NotNull
    private String content;

    private int commentOrder;
}

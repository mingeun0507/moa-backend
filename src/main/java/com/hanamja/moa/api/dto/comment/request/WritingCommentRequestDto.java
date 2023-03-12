package com.hanamja.moa.api.dto.comment.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WritingCommentRequestDto {

    @NotNull
    private String content;

    /* 작성, 수정은 이거로 통합해서 쓰면 될듯? */
}

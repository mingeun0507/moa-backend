package com.hanamja.moa.api.dto.post_comment.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ModifyPostCommentRequestDto {

        private String content;

        private int commentOrder;
}

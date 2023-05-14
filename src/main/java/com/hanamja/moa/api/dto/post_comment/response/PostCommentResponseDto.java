package com.hanamja.moa.api.dto.post_comment.response;

import com.hanamja.moa.api.dto.user.response.UserInfoResponseDto;
import com.hanamja.moa.api.entity.post_comment.PostComment;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PostCommentResponseDto {

    private Long id;

    private Long postId;

    private Long parentCommentId;

    private UserInfoResponseDto writer;

    private String content;

    private boolean isModified;

    private boolean isDeleted;

    private boolean isReply;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    public static PostCommentResponseDto from(PostComment postComment) {
        return new PostCommentResponseDto(
                postComment.getId(),
                postComment.getPost().getId(),
                postComment.getParentComment() != null ? postComment.getParentComment().getId() : null,
                UserInfoResponseDto.from(postComment.getUser()),
                postComment.getContent(),
                postComment.isModified(),
                postComment.isDeleted(),
                postComment.isReply(),
                postComment.getCreatedAt(),
                postComment.getModifiedAt()
        );
    }

}

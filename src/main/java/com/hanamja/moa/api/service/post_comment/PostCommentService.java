package com.hanamja.moa.api.service.post_comment;

import com.hanamja.moa.api.dto.post_comment.request.CreatePostCommentRequestDto;
import com.hanamja.moa.api.dto.post_comment.request.ModifyPostCommentRequestDto;
import com.hanamja.moa.api.dto.post_comment.response.PostCommentResponseDto;

public interface PostCommentService {

    PostCommentResponseDto createPostComment(Long postId, Long userId, CreatePostCommentRequestDto requestDto);

    PostCommentResponseDto updatePostComment(Long postId, Long commentId, Long userId, ModifyPostCommentRequestDto requestDto);

    PostCommentResponseDto deletePostComment(Long postId, Long commentId, Long userId);
}

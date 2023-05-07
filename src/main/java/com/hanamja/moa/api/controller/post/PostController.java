package com.hanamja.moa.api.controller.post;

import com.hanamja.moa.api.dto.post_comment.request.CreatePostCommentRequestDto;
import com.hanamja.moa.api.dto.post_comment.request.ModifyPostCommentRequestDto;
import com.hanamja.moa.api.dto.post_comment.response.PostCommentResponseDto;
import com.hanamja.moa.api.entity.post.Post;
import com.hanamja.moa.api.entity.post.PostRepository;
import com.hanamja.moa.api.entity.post_comment.PostComment;
import com.hanamja.moa.api.entity.post_comment.PostCommentRepository;
import com.hanamja.moa.api.entity.user.UserAccount.UserAccount;
import com.hanamja.moa.api.service.post_comment.PostCommentService;
import com.hanamja.moa.exception.custom.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/board")
public class PostController {

    private final PostCommentService postCommentService;

    @Operation(summary = "게시판 댓글 달기", description = "게시판 댓글 달기")
    @PostMapping("/{postId}/comment")
    public ResponseEntity<PostCommentResponseDto> createPostComment(
            @Parameter(hidden = true) @AuthenticationPrincipal UserAccount userAccount,
            @PathVariable("postId") Long postId,
            @RequestBody CreatePostCommentRequestDto requestDto
    ) {
        PostCommentResponseDto responseDto = postCommentService.createPostComment(
                postId,
                userAccount.getUserId(),
                userAccount.getDepartmentId(),
                requestDto
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @Operation(summary = "게시판 댓글 수정", description = "게시판 댓글 수정")
    @PutMapping("{postId}/comment/{commentId}")
    public ResponseEntity<PostCommentResponseDto> modifyPostComment(
            @Parameter(hidden = true) @AuthenticationPrincipal UserAccount userAccount,
            @PathVariable("postId") Long postId,
            @PathVariable("commentId") Long commentId,
            @RequestBody ModifyPostCommentRequestDto requestDto
    ) {
        PostCommentResponseDto responseDto = postCommentService.updatePostComment(
                postId,
                commentId,
                userAccount.getDepartmentId(),
                userAccount.getUserId(),
                requestDto
        );

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "게시판 댓글 달기", description = "게시판 댓글 달기")
    @DeleteMapping("{postId}/comment/{commentId}")
    public ResponseEntity<PostCommentResponseDto> deletePostComment(
            @Parameter(hidden = true) @AuthenticationPrincipal UserAccount userAccount,
            @PathVariable("postId") Long postId,
            @PathVariable("commentId") Long commentId
    ) {
        PostCommentResponseDto responseDto = postCommentService.deletePostComment(
                postId,
                commentId,
                userAccount.getDepartmentId(),
                userAccount.getUserId()
        );

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

}

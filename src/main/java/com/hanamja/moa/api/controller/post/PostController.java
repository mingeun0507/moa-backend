package com.hanamja.moa.api.controller.post;

import com.hanamja.moa.api.dto.post.request.CreatePostRequestDto;
import com.hanamja.moa.api.dto.post.request.EditPostRequestDto;
import com.hanamja.moa.api.dto.post.response.CreatePostResponseDto;
import com.hanamja.moa.api.dto.post.response.PostDetailInfoResponseDto;
import com.hanamja.moa.api.dto.post.response.PostImageResponseDto;
import com.hanamja.moa.api.dto.post_comment.request.CreatePostCommentRequestDto;
import com.hanamja.moa.api.dto.post_comment.request.ModifyPostCommentRequestDto;
import com.hanamja.moa.api.dto.post_comment.response.PostCommentResponseDto;
import com.hanamja.moa.api.dto.util.DataResponseDto;
import com.hanamja.moa.api.entity.user.UserAccount.UserAccount;
import com.hanamja.moa.api.service.post.PostService;
import com.hanamja.moa.api.service.post_comment.PostCommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/post")
public class PostController {

    private final PostCommentService postCommentService;

    private final PostService postService;

    @GetMapping("/{postId}")
    public ResponseEntity<DataResponseDto<PostDetailInfoResponseDto>> getPostDetailInfo
            (
                    @Parameter(hidden = true) @AuthenticationPrincipal UserAccount userAccount,
                    @PathVariable @NotNull Long postId
            ) {
        return ResponseEntity.ok(postService.getPostDetailInfo(userAccount, postId));
    }

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

    @PostMapping
    public ResponseEntity<CreatePostResponseDto> createPost
            (
                    @Parameter(hidden = true) @AuthenticationPrincipal UserAccount userAccount,
                    @RequestBody @NotNull CreatePostRequestDto createPostRequestDto
            ) {
        CreatePostResponseDto createPostResponseDto = postService.createNewPost(userAccount, createPostRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(createPostResponseDto);
    }

    @PostMapping(value = "/image")
    public ResponseEntity<PostImageResponseDto> uploadNewBoardPostImage
            (
                    @Parameter(hidden = true) @AuthenticationPrincipal UserAccount userAccount,
                    @RequestPart @Nullable MultipartFile image
            ) {
        PostImageResponseDto postImageResponseDto = postService.uploadImage(userAccount, image);
        return ResponseEntity.status(HttpStatus.OK).body(postImageResponseDto);
    }

    @PutMapping
    public ResponseEntity<CreatePostResponseDto> editPost
            (
                    @Parameter(hidden = true) @AuthenticationPrincipal UserAccount userAccount,
                    @RequestBody @NotNull EditPostRequestDto editPostRequestDto
            ) {
        CreatePostResponseDto createPostResponseDto = postService.editPost(userAccount, editPostRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(createPostResponseDto);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> removeBoardPost
            (
                    @Parameter(hidden = true) @AuthenticationPrincipal UserAccount userAccount,
                    @PathVariable @NotNull Long postId) {
        postService.deleteBoardPost(postId, userAccount);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<?> clickLikeBoardPost
            (
                    @Parameter(hidden = true) @AuthenticationPrincipal UserAccount userAccount,
                    @PathVariable @NotNull Long postId
            )
    {
        postService.likeBoardPost(postId, userAccount);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/{postId}/bookmark")
    public ResponseEntity<?> makeBookmarkBoardPost
            (
                    @Parameter(hidden = true) @AuthenticationPrincipal UserAccount userAccount,
                    @PathVariable @NotNull Long postId
            )
    {
        postService.bookmarkBoardPost(postId, userAccount);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

package com.hanamja.moa.api.controller.post;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanamja.moa.api.dto.post.request.BoardPostSaveAndEditRequestDto;
import com.hanamja.moa.api.dto.post.response.PostDetailInfoResponseDto;
import com.hanamja.moa.api.dto.util.DataResponseDto;
import com.hanamja.moa.api.entity.user.UserAccount.UserAccount;
import com.hanamja.moa.api.service.post.PostServiceImpl;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/post")
public class PostController {

    private final PostServiceImpl postService;

    @GetMapping("/{postId}")
    public ResponseEntity<DataResponseDto<PostDetailInfoResponseDto>> getPostDetailInfo
            (
                    @Parameter(hidden = true) @AuthenticationPrincipal UserAccount userAccount,
                    @PathVariable @NotNull Long postId
            ) {
        return ResponseEntity.ok(postService.getPostDetailInfo(userAccount, postId));
    }

    @PostMapping
    public ResponseEntity<?> makeNewBoardPost
            (
                    @Parameter(hidden = true) @AuthenticationPrincipal UserAccount userAccount,
                    @RequestPart @Nullable List<MultipartFile> images,
                    @RequestPart @NotNull String data
            ) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        BoardPostSaveAndEditRequestDto boardPostSaveAndEditRequestDto = objectMapper.readValue(data, BoardPostSaveAndEditRequestDto.class);
        postService.registerNewBoardPost(userAccount, images, boardPostSaveAndEditRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{postId}")
    public ResponseEntity<?> editBoardPost
            (
                    @Parameter(hidden = true) @AuthenticationPrincipal UserAccount userAccount,
                    @PathVariable @NotNull Long postId,
                    @RequestPart @Nullable List<MultipartFile> images,
                    @RequestPart @NotNull String data
            ) throws JsonProcessingException
    {
        ObjectMapper objectMapper = new ObjectMapper();
        BoardPostSaveAndEditRequestDto boardPostSaveAndEditRequestDto = objectMapper.readValue(data, BoardPostSaveAndEditRequestDto.class);
        postService.editBoardPost(postId, userAccount, images, boardPostSaveAndEditRequestDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> removeBoardPost
    (
            @Parameter(hidden = true) @AuthenticationPrincipal UserAccount userAccount,
            @PathVariable @NotNull Long postId)
    {
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

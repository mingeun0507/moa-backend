package com.hanamja.moa.api.service.post;

import com.hanamja.moa.api.dto.post.request.BoardPostEditRequestDto;
import com.hanamja.moa.api.dto.post.request.BoardPostSaveRequestDto;
import com.hanamja.moa.api.dto.post.response.CreatePostImageResponseDto;
import com.hanamja.moa.api.dto.post.response.CreatePostResponseDto;
import com.hanamja.moa.api.entity.board_category.BoardCategory;
import com.hanamja.moa.api.entity.board_category.BoardCategoryRepository;
import com.hanamja.moa.api.entity.department.Department;
import com.hanamja.moa.api.entity.post.Post;
import com.hanamja.moa.api.entity.post.PostRepository;
import com.hanamja.moa.api.entity.post_bookmark.PostBookmark;
import com.hanamja.moa.api.entity.post_bookmark.PostBookmarkRepository;
import com.hanamja.moa.api.entity.post_comment.PostCommentRepository;
import com.hanamja.moa.api.entity.post_image.PostImage;
import com.hanamja.moa.api.entity.post_image.PostImageRepository;
import com.hanamja.moa.api.entity.post_like.PostLike;
import com.hanamja.moa.api.entity.post_like.PostLikeRepository;
import com.hanamja.moa.api.entity.user.User;
import com.hanamja.moa.api.entity.user.UserAccount.UserAccount;
import com.hanamja.moa.api.service.util.UtilServiceImpl;
import com.hanamja.moa.exception.custom.CustomException;
import com.hanamja.moa.exception.custom.NotFoundException;
import com.hanamja.moa.utils.s3.AmazonS3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class PostServiceImpl implements PostService {
    private final BoardCategoryRepository boardCategoryRepository;
    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostCommentRepository postCommentRepository;
    private final PostBookmarkRepository postBookmarkRepository;
    private final UtilServiceImpl utilService;
    private final AmazonS3Uploader amazonS3Uploader;
    @Override
    @Transactional(readOnly = true)
    public BoardCategory resolveBoardCategoryById(Long boardCategoryId) {
        return boardCategoryRepository
                .findById(boardCategoryId)
                .orElseThrow(
                        () -> NotFoundException
                                .builder()
                                .httpStatus(HttpStatus.NOT_FOUND)
                                .message("해당 카테고리가 존재하지 않습니다.")
                                .build()
                );
    }

    @Override
    @Transactional(readOnly = true)
    public Post resolvePostByIdAndUserId(Long postId, Long userId) {
        return postRepository
                .findByIdAndUser_Id(postId, userId)
                .orElseThrow(
                        () -> NotFoundException
                                .builder()
                                .httpStatus(HttpStatus.BAD_REQUEST)
                                .message("해당 게시글이 존재하지 않거나 게시글 소유자가 아닙니다.")
                                .build()
                );
    }

    @Override
    @Transactional(readOnly = true)
    public Post resolvePostById(Long postId) {
        return postRepository
                .findById(postId)
                .orElseThrow(
                        () -> NotFoundException
                                .builder()
                                .httpStatus(HttpStatus.NOT_FOUND)
                                .message("해당 게시글이 존재하지 않습니다.")
                                .build()
                );
    }

    @Override
    public String registerImageToPostImage(MultipartFile image) {
        String imageUrl = "";
        try {
            imageUrl = amazonS3Uploader.saveFileAndGetUrl(image);
        } catch (Exception e) {
            throw CustomException.builder().httpStatus(HttpStatus.EXPECTATION_FAILED).message("이미지 업로드에 실패했습니다").build();
        }
        return imageUrl;
    }

    @Override
    @Transactional
    public CreatePostResponseDto registerNewBoardPost(UserAccount userAccount, BoardPostSaveRequestDto boardPostSaveRequestDto) {
        User user = utilService.resolveUserById(userAccount);
        Department department = utilService.resolveDepartmentById(userAccount);
        BoardCategory boardCategory = resolveBoardCategoryById(boardPostSaveRequestDto.getCategoryId());

        Post newPost = postRepository.save(Post.builder()
                .user(user)
                .department(department)
                .boardCategory(boardCategory)
                .title(boardPostSaveRequestDto.getTitle())
                .content(boardPostSaveRequestDto.getContent())
                .thumbnail(!boardPostSaveRequestDto.getImages().isEmpty() ? boardPostSaveRequestDto.getImages().get(0) : null)
                .build());

        if (!boardPostSaveRequestDto.getImages().isEmpty()){
            for (String imageUrl : boardPostSaveRequestDto.getImages()) {
                postImageRepository.save(PostImage.builder()
                        .post(newPost)
                        .image(imageUrl)
                        .build());
            }
        }

        return CreatePostResponseDto.builder()
                .postId(newPost.getId()).title(newPost.getTitle()).content(newPost.getContent()).thumbnail(newPost.getThumbnail())
                .createdAt(newPost.getCreatedAt()).updatedAt(newPost.getModifiedAt())
                .build();
    }

    @Override
    public CreatePostImageResponseDto uploadImage(UserAccount userAccount, MultipartFile image){
        utilService.resolveUserById(userAccount);
        utilService.resolveDepartmentById(userAccount);
        String imageUrl = registerImageToPostImage(image);
        return CreatePostImageResponseDto.builder().imageUrl(imageUrl).build();
    }

    @Override
    @Transactional
    public CreatePostResponseDto editBoardPost(UserAccount userAccount, BoardPostEditRequestDto boardPostEditRequestDto) {
        Post existPost = resolvePostByIdAndUserId(boardPostEditRequestDto.getPostId(), userAccount.getUserId());
        // 최종 전달된 이미지 url 에 포함되어있으면 삭제
        List<String> images = boardPostEditRequestDto.getImages();
        List<PostImage> allExistImagesByImageUrl = postImageRepository.findAllExistByImageUrl(images);
        allExistImagesByImageUrl.forEach(postImage -> {
                    amazonS3Uploader.deleteFile(postImage.getImage());
                    postImageRepository.delete(postImage);
                });

        // 새로 추가된 이미지 url 이 있으면 추가
        if (!boardPostEditRequestDto.getImages().isEmpty()){
            List<String> existsImages = allExistImagesByImageUrl.stream().map(PostImage::getImage).collect(Collectors.toList());
            images.forEach(image -> {
                if (!existsImages.contains(image)){
                    postImageRepository.save(PostImage.builder()
                            .post(existPost)
                            .image(image)
                            .build());
                }
            });
        }

        BoardCategory boardCategory = resolveBoardCategoryById(boardPostEditRequestDto.getCategoryId());

        existPost.updatePostInfo(
                boardPostEditRequestDto,
                boardCategory,
                boardPostEditRequestDto.getImages().isEmpty() ? null : boardPostEditRequestDto.getImages().get(0)
                );

        return CreatePostResponseDto.builder()
                .postId(existPost.getId()).title(existPost.getTitle()).content(existPost.getContent()).thumbnail(existPost.getThumbnail())
                .createdAt(existPost.getCreatedAt()).updatedAt(existPost.getModifiedAt())
                .build();
    }

    @Override
    @Transactional
    public void deleteBoardPost(Long postId, UserAccount userAccount) {
        Post existPost = resolvePostByIdAndUserId(postId, userAccount.getUserId());

        postImageRepository.deleteAllByPost_Id(postId);
        postLikeRepository.deleteAllByPost(existPost);
        postCommentRepository.deleteAllByPost(existPost);
        postBookmarkRepository.deleteAllByPost(existPost);
        postRepository.delete(existPost);
    }

    @Override
    @Transactional
    public void likeBoardPost(Long postId, UserAccount userAccount) {
        Optional<PostLike> postLike = postLikeRepository.findByPost_IdAndUser_Id(postId, userAccount.getUserId());
        if (postLike.isPresent()){
            postLikeRepository.delete(postLike.get());
        } else {
            postLikeRepository.save(PostLike.builder()
                            .user(utilService.resolveUserById(userAccount))
                            .post(resolvePostById(postId))
                            .build());
        }

    }

    @Override
    public void bookmarkBoardPost(Long postId, UserAccount userAccount) {
        Optional<PostBookmark> bookmark = postBookmarkRepository.findByPost_IdAndUser_Id(postId, userAccount.getUserId());
        if (bookmark.isPresent()) {
            postBookmarkRepository.delete(bookmark.get());
        } else {
            postBookmarkRepository.save(PostBookmark.builder()
                            .user(utilService.resolveUserById(userAccount))
                            .post(resolvePostById(postId))
                            .build());
        }
    }
}

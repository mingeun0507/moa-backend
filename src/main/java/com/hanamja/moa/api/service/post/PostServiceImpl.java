package com.hanamja.moa.api.service.post;

import com.hanamja.moa.api.dto.post.request.BoardPostSaveAndEditRequestDto;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public List<String> registerImagesToPostImage(List<MultipartFile> images) {
        List<String> imageUrls = new ArrayList<>();
        images.forEach(image -> {
            Optional<String> imageUrl;
            try {
                imageUrl = Optional.ofNullable(amazonS3Uploader.saveFileAndGetUrl(image));
                imageUrls.add(imageUrl.orElseThrow(
                        () -> CustomException.builder().httpStatus(HttpStatus.EXPECTATION_FAILED).message("이미지 업로드에 실패했습니다").build()
                ));
            } catch (Exception e) {
                throw CustomException.builder().httpStatus(HttpStatus.EXPECTATION_FAILED).message("이미지 업로드에 실패했습니다").build();
            }
        });
        return imageUrls;
    }

    @Override
    @Transactional
    public void registerNewBoardPost(UserAccount userAccount, List<MultipartFile> images, BoardPostSaveAndEditRequestDto boardPostSaveAndEditRequestDto) {
        List<String> imagesUrls = new ArrayList<>();
        User user = utilService.resolveUserById(userAccount);
        Department department = utilService.resolveDepartmentById(userAccount);
        BoardCategory boardCategory = resolveBoardCategoryById(boardPostSaveAndEditRequestDto.getCategoryId());

        if (images != null) {
            imagesUrls = registerImagesToPostImage(images);
        }

        Post newPost = postRepository.save(Post.builder()
                .user(user)
                .department(department)
                .boardCategory(boardCategory)
                .title(boardPostSaveAndEditRequestDto.getTitle())
                .content(boardPostSaveAndEditRequestDto.getContent())
                .thumbnail(imagesUrls.size() > 0 ? imagesUrls.get(0) : null)
                .build());

        imagesUrls.forEach(imageUrl -> {
            postImageRepository.save(PostImage.builder()
                            .post(newPost).image(imageUrl)
                            .build());
        });
    }

    @Override
    @Transactional
    public void editBoardPost(Long postId, UserAccount userAccount, List<MultipartFile> images, BoardPostSaveAndEditRequestDto boardPostSaveAndEditRequestDto) {
        List<String> imagesUrls = new ArrayList<>();
        Post existPost = resolvePostByIdAndUserId(postId, userAccount.getUserId());
        postImageRepository.deleteAllByPost_Id(postId);

        if (images != null) {
            imagesUrls = registerImagesToPostImage(images);
        }
        BoardCategory boardCategory = resolveBoardCategoryById(boardPostSaveAndEditRequestDto.getCategoryId());

        existPost.updatePostInfo(
                boardPostSaveAndEditRequestDto,
                boardCategory,
                imagesUrls.size() > 0 ? imagesUrls.get(0) : ""
                );

        imagesUrls.forEach(imageUrl -> {
            postImageRepository.save(PostImage.builder()
                    .post(existPost).image(imageUrl)
                    .build());
        });
    }

    @Override
    @Transactional
    public void deleteBoardPost(Long postId, UserAccount userAccount) {
        Post existPost = resolvePostByIdAndUserId(postId, userAccount.getUserId());

        postImageRepository.deleteAllByPost_Id(postId);
        postRepository.delete(existPost);
        postLikeRepository.deleteAllByPost(existPost);
        postCommentRepository.deleteAllByPost(existPost);
        postBookmarkRepository.deleteAllByPost(existPost);
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

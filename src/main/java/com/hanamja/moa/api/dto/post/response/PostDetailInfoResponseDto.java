package com.hanamja.moa.api.dto.post.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostDetailInfoResponseDto {
    @JsonProperty("post_id")
    private Long postId;
    @JsonProperty("title")
    private String title;
    @JsonProperty("content")
    private String content;
    @JsonProperty("image_list")
    private List<String> imageList;
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("user_name")
    private String userName;
    @JsonProperty("user_profile_image")
    private String userProfileImage;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    @JsonProperty("category_id")
    private Long categoryId;
    @JsonProperty("category_name")
    private String categoryName;
    @JsonProperty("is_liked")
    private Boolean isLiked;
    @JsonProperty("like_count")
    private Integer likeCount;
    @JsonProperty("comment_count")
    private Integer commentCount;

    @Builder
    public PostDetailInfoResponseDto(Long postId, String title, String content, List<String> imageList, Long userId, String userName, String userProfileImage, LocalDateTime createdAt, Long categoryId, String categoryName, Boolean isLiked, Integer likeCount, Integer commentCount) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.imageList = imageList;
        this.userId = userId;
        this.userName = userName;
        this.userProfileImage = userProfileImage;
        this.createdAt = createdAt;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.isLiked = isLiked;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
    }

    public static PostDetailInfoResponseDto from(Long postId, String title, String content, List<String> imageList, Long userId, String userName, String userProfileImage, LocalDateTime createdAt, Long categoryId, String categoryName, Boolean isLiked, Integer likeCount, Integer commentCount) {
        return PostDetailInfoResponseDto.builder()
                .postId(postId)
                .title(title)
                .content(content)
                .imageList(imageList)
                .userId(userId)
                .userName(userName)
                .userProfileImage(userProfileImage)
                .createdAt(createdAt)
                .categoryId(categoryId)
                .categoryName(categoryName)
                .isLiked(isLiked)
                .likeCount(likeCount)
                .commentCount(commentCount)
                .build();
    }
}

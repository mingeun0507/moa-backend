package com.hanamja.moa.api.dto.post.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostInfoResponseDto {
    private Long postId;
    private String title;
    private String content;
    private String thumbnail;
    private LocalDateTime createdDate;
    private SimpleUserInfo userInfo;
    private SimpleCategoryInfo categoryInfo;
    private Integer likeCount;
    private Integer commentCount;

    @Builder
    public PostInfoResponseDto(Long postId, String title, String content, String thumbnail, LocalDateTime createdDate, SimpleUserInfo userInfo, SimpleCategoryInfo categoryInfo, Integer likeCount, Integer commentCount) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.thumbnail = thumbnail;
        this.createdDate = createdDate;
        this.userInfo = userInfo;
        this.categoryInfo = categoryInfo;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
    }

    public PostInfoResponseDto(Long postId, String title, String content, String thumbnail, LocalDateTime createdDate, Long userId, String userName, String userProfileImage, Long categoryId, String categoryName, Integer likeCount, Integer commentCount) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.thumbnail = thumbnail;
        this.createdDate = createdDate;
        this.userInfo = SimpleUserInfo.from(userId, userName, userProfileImage);
        this.categoryInfo = SimpleCategoryInfo.from(categoryId, categoryName);
        this.likeCount = likeCount;
        this.commentCount = commentCount;
    }

    public static PostInfoResponseDto from(Long postId, String title, String content, String thumbnail, LocalDateTime createdDate, Long userId, String userName, String userProfileImage, Long categoryId, String categoryName, Integer likeCount, Integer commentCount) {
        return PostInfoResponseDto.builder()
                .postId(postId)
                .title(title)
                .content(content)
                .thumbnail(thumbnail)
                .createdDate(createdDate)
                .userInfo(SimpleUserInfo.from(userId, userName, userProfileImage))
                .categoryInfo(SimpleCategoryInfo.from(categoryId, categoryName))
                .likeCount(likeCount)
                .commentCount(commentCount)
                .build();
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    static class SimpleUserInfo {
        private Long userId;
        private String userName;
        private String userProfileImage;

        @Builder
        public SimpleUserInfo(Long userId, String userName, String userProfileImage) {
            this.userId = userId;
            this.userName = userName;
            this.userProfileImage = userProfileImage;
        }

        public static SimpleUserInfo from(Long userId, String userName, String userProfileImage) {
            return SimpleUserInfo.builder()
                    .userId(userId)
                    .userName(userName)
                    .userProfileImage(userProfileImage)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    static class SimpleCategoryInfo {
        private Long categoryId;
        private String categoryName;

        @Builder
        public SimpleCategoryInfo(Long categoryId, String categoryName) {
            this.categoryId = categoryId;
            this.categoryName = categoryName;
        }

        public static SimpleCategoryInfo from(Long categoryId, String categoryName) {
            return SimpleCategoryInfo.builder()
                    .categoryId(categoryId)
                    .categoryName(categoryName)
                    .build();
        }
    }
}

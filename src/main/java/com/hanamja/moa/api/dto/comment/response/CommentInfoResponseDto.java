package com.hanamja.moa.api.dto.comment.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentInfoResponseDto {

    private Long id;
    private String content;
    private String createdAt;
    private String modifiedAt;
    private Boolean isModified;
    private Long userId;
    private String userName;
    private String userImage;

    @Builder
    public CommentInfoResponseDto(Long id, String content, String createdAt, String modifiedAt, Boolean isModified, Long userId, String userName, String userImage) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.isModified = isModified;
        this.userId = userId;
        this.userName = userName;
        this.userImage = userImage;
    }

    public static CommentInfoResponseDto from(com.hanamja.moa.api.entity.comment.Comment comment) {
        return CommentInfoResponseDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt().toString())
                .modifiedAt(comment.getModifiedAt().toString())
                .isModified(comment.getIsModified())
                .userId(comment.getUser().getId())
                .userName(comment.getUser().getName())
                .userImage(comment.getUser().getImageLink())
                .build();
    }
}

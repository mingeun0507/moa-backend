package com.hanamja.moa.api.dto.notification.response;

import com.hanamja.moa.api.entity.notification.Notification;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationResponseDto {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private String reason;
    private Boolean isBadged;

    @Builder
    public NotificationResponseDto(Long id, String content, LocalDateTime createdAt, String reason, Boolean isBadged) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.reason = reason;
        this.isBadged = isBadged;
    }

    public static NotificationResponseDto from(Notification notification) {
        return NotificationResponseDto.builder()
                .id(notification.getId())
                .content(notification.getContent())
                .createdAt(notification.getCreatedAt())
                .reason(notification.getReason())
                .isBadged(notification.getIsBadged())
                .build();
    }
}

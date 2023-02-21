package com.hanamja.moa.api.dto.point_history.response;

import com.hanamja.moa.api.entity.point_history.PointHistory;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointHistoryInfoResponseDto {
    private Long id;
    private String title;
    private Long point;
    private String cardMessage;
    private String groupMessage;
    private LocalDateTime createdAt;

    @Builder
    public PointHistoryInfoResponseDto(Long id, String title, Long point, String cardMessage, String groupMessage, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.point = point;
        this.cardMessage = cardMessage;
        this.groupMessage = groupMessage;
        this.createdAt = createdAt;
    }

    public static PointHistoryInfoResponseDto from(PointHistory pointHistory) {
        String[] splitMessages = pointHistory.getMessage().split("\n");
        return PointHistoryInfoResponseDto
                .builder()
                .id(pointHistory.getId())
                .title(pointHistory.getTitle())
                .point(pointHistory.getPoint())
                .cardMessage(splitMessages[0])
                .groupMessage(splitMessages[1])
                .createdAt(pointHistory.getCreatedAt())
                .build();
    }
}

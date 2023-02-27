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
    private String groupMessage;
    private String cardMessage;
    private String totalPointMessage;
    private LocalDateTime createdAt;

    @Builder
    public PointHistoryInfoResponseDto(Long id, String title, Long point, String cardMessage, String groupMessage, LocalDateTime createdAt, String totalPointMessage) {
        this.id = id;
        this.title = title;
        this.point = point;
        this.cardMessage = cardMessage;
        this.groupMessage = groupMessage;
        this.createdAt = createdAt;
        this.totalPointMessage = totalPointMessage;
    }

    public static PointHistoryInfoResponseDto from(PointHistory pointHistory) {
        String[] splitMessages = pointHistory.getMessage().split("\n");
        return PointHistoryInfoResponseDto
                .builder()
                .id(pointHistory.getId())
                .title(pointHistory.getTitle())
                .point(pointHistory.getPoint())
                .groupMessage(splitMessages[0])
                .cardMessage(splitMessages[1])
                .totalPointMessage(splitMessages[2])
                .createdAt(pointHistory.getCreatedAt())
                .build();
    }
}

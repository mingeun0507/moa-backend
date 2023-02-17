package com.hanamja.moa.api.dto.history.response;

import com.hanamja.moa.api.entity.history.History;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HistoryInfoResponseDto {
    private Long id;
    private String title;
    private Long point;
    private LocalDateTime createdAt;

    @Builder
    public HistoryInfoResponseDto(Long id, String title, Long point, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.point = point;
        this.createdAt = createdAt;
    }

    public static HistoryInfoResponseDto from(History history) {
        return HistoryInfoResponseDto
                .builder()
                .id(history.getId())
                .title(history.getTitle())
                .point(history.getPoint())
                .createdAt(history.getCreatedAt())
                .build();
    }
}

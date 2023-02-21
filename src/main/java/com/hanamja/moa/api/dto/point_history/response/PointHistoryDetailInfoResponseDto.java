package com.hanamja.moa.api.dto.point_history.response;

import com.hanamja.moa.api.entity.point_history.PointHistory;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointHistoryDetailInfoResponseDto {
    private String message;
    private PointHistoryInfoResponseDto pointHistoryInfoResponseDto;

    @Builder
    public PointHistoryDetailInfoResponseDto(String message, PointHistoryInfoResponseDto pointHistoryInfoResponseDto) {
        this.message = message;
        this.pointHistoryInfoResponseDto = pointHistoryInfoResponseDto;
    }

    public static PointHistoryDetailInfoResponseDto from(PointHistory pointHistory) {
        return PointHistoryDetailInfoResponseDto
                .builder()
                .message(pointHistory.getMessage())
                .pointHistoryInfoResponseDto(PointHistoryInfoResponseDto.from(pointHistory))
                .build();
    }
}

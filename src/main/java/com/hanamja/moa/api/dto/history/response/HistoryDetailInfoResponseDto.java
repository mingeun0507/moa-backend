package com.hanamja.moa.api.dto.history.response;

import com.hanamja.moa.api.entity.history.History;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HistoryDetailInfoResponseDto {
    private String message;
    private HistoryInfoResponseDto historyInfoResponseDto;

    @Builder
    public HistoryDetailInfoResponseDto(String message, HistoryInfoResponseDto historyInfoResponseDto) {
        this.message = message;
        this.historyInfoResponseDto = historyInfoResponseDto;
    }

    public static HistoryDetailInfoResponseDto from(History history) {
        return HistoryDetailInfoResponseDto
                .builder()
                .message(history.getMessage())
                .historyInfoResponseDto(HistoryInfoResponseDto.from(history))
                .build();
    }
}

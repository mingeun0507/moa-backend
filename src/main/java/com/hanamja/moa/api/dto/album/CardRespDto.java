package com.hanamja.moa.api.dto.album;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class CardRespDto {
    @JsonProperty(value = "user_id")
    private Long userId;

    private String username;

    @JsonProperty(value = "meeting_cnt")
    private Long meetingCnt;

    private List<CardInfo> cards;

    @Builder
    public static class CardInfo{
        @JsonProperty(value = "meeting_at")
        private LocalDateTime meetingAt;

        @JsonProperty(value = "front_image")
        private String frontImage;

        @JsonProperty(value = "back_image")
        private String backImage;
    }

    @Builder
    public CardRespDto(Long userId, String username, Long meetingCnt, List<CardInfo> cards) {
        this.userId = userId;
        this.username = username;
        this.meetingCnt = meetingCnt;
        this.cards = cards;
    }
}

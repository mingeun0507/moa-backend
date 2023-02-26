package com.hanamja.moa.api.dto.group.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupCompleteRespDto {
    @JsonProperty(value = "card_list")
    private List<Card> cardList;

    @Builder
    public static class Card{
        @JsonProperty(value = "user_id")
        private Long userId;

        private String username;

        @JsonProperty(value = "meeting_cnt")
        private Long meetingCnt;

        @JsonProperty(value = "meeting_at")
        private LocalDateTime meetingAt;

        @JsonProperty(value = "front_image")
        private String frontImage;

        @JsonProperty(value = "back_image")
        private String backImage;
    }

    @Builder
    public GroupCompleteRespDto(List<Card> cardList) {
        this.cardList = cardList;
    }

}

package com.hanamja.moa.api.dto.group.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupCompleteRespDto {
    @JsonProperty(value = "card_list")
    private List<Card> cardList;

    public static class Card{
        @JsonProperty(value = "user_id")
        private Long userId;

        @JsonProperty(value = "username")
        private String username;

        @JsonProperty(value = "meeting_cnt")
        private Long meetingCnt;

        @JsonProperty(value = "meeting_at")
        private LocalDateTime meetingAt;

        @JsonProperty(value = "front_image")
        private String frontImage;

        @JsonProperty(value = "back_image")
        private String backImage;

        @Builder
        public Card(Long userId, String username, Long meetingCnt, Optional<LocalDateTime> meetingAt, String frontImage, String backImage) {
            this.userId = userId;
            this.username = username;
            this.meetingCnt = meetingCnt;
            this.meetingAt = meetingAt.isPresent() ? meetingAt.get() : LocalDateTime.now();
            this.frontImage = frontImage;
            this.backImage = backImage;
        }
    }

    @Builder
    public GroupCompleteRespDto(List<Card> cardList) {
        this.cardList = cardList;
    }

}

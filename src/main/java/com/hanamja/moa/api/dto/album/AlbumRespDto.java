package com.hanamja.moa.api.dto.album;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AlbumRespDto {
    @JsonProperty(value = "user_id")
    private Long userId;

    private String username;

    @JsonProperty(value = "image_link")
    private String imageLink;

    @JsonProperty(value = "meeting_cnt")
    private int meetingCnt;

    @JsonProperty(value = "is_badged")
    private boolean isBadged;

    @Builder
    public AlbumRespDto(Long userId, String username, String imageLink, int meetingCnt, boolean isBadged) {
        this.userId = userId;
        this.username = username;
        this.imageLink = imageLink;
        this.meetingCnt = meetingCnt;
        this.isBadged = isBadged;
    }
}

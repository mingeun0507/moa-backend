package com.hanamja.moa.api.dto.group.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class CardRespDto {
    @JsonProperty(value = "image_link")
    private String imageLink;
    private String date;

    @Builder
    public CardRespDto(String imageLink, String date) {
        this.imageLink = imageLink;
        this.date = date;
    }
}

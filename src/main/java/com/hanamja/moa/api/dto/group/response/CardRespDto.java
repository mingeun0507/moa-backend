package com.hanamja.moa.api.dto.group.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
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

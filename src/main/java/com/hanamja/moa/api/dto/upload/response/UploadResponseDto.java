package com.hanamja.moa.api.dto.upload.response;

import lombok.Getter;

@Getter
public class UploadResponseDto {
    private String url;

    public UploadResponseDto(String url) {
        this.url = url;
    }
}

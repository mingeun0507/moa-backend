package com.hanamja.moa.api.dto.auth.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OnBoardingRequestDto {
    private String gender;
    private String department;
    private String imageLink;

    @Builder
    public OnBoardingRequestDto(String gender, String department, String imageLink) {
        this.gender = gender;
        this.department = department;
        this.imageLink = imageLink;
    }
}

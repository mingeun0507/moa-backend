package com.hanamja.moa.api.dto.auth.request;

import com.hanamja.moa.api.entity.user.Gender;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OnBoardingRequestDto {


    @NotNull
    private Gender gender;

    @NotNull
    private String department;
    private String imageLink;

    @Builder
    public OnBoardingRequestDto(Gender gender, String department, String imageLink) {
        this.gender = gender;
        this.department = department;
        this.imageLink = imageLink;
    }
}

package com.hanamja.moa.api.dto.auth.request;

import com.hanamja.moa.api.entity.user.Gender;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OnBoardingRequestDto {


    @NotNull
    private Gender gender;

    @NotNull
    private List<String> departments;
    private String imageLink;

    @Builder
    public OnBoardingRequestDto(Gender gender, List<String> departments, String imageLink) {
        this.gender = gender;
        this.departments = departments;
        this.imageLink = imageLink;
    }
}

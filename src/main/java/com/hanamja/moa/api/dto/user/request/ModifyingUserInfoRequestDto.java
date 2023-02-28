package com.hanamja.moa.api.dto.user.request;

import com.hanamja.moa.api.entity.user.Gender;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ModifyingUserInfoRequestDto {
    @NotNull
    private Gender gender;
    @NotNull
    private String department;
    private String imageLink;
    private String intro;

    @Builder
    public ModifyingUserInfoRequestDto(Gender gender, String department, String imageLink, String intro) {
        this.gender = gender;
        this.department = department;
        this.imageLink = imageLink;
        this.intro = intro;
    }
}

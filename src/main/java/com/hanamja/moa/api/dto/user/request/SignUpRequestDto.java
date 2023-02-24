package com.hanamja.moa.api.dto.user.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignUpRequestDto {
    @NotNull
    @Pattern(regexp = "^[0-9]{8}$", message = "학번은 8자리 숫자로 입력해주세요.")
    private String studentId;
    @NotNull
    @Pattern(regexp = "^[가-힣]{2,10}$", message = "이름은 2 ~ 10자의 한글로 입력해주세요.")
    private String name;
    @NotNull
    private String password;

    public SignUpRequestDto(String studentId, String name, String password) {
        this.studentId = studentId;
        this.name = name;
        this.password = password;
    }
}

package com.hanamja.moa.api.dto.user.response;

import com.hanamja.moa.api.entity.user.Gender;
import com.hanamja.moa.api.entity.user.Role;
import com.hanamja.moa.api.entity.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserInfoResponseDto {
    private Long id;
    private String studentId;
    private String name;
    private Gender gender;
    private String imageLink;
    private Long point;
    private String intro;
    private Role role;
    private String department;

    @Builder
    public UserInfoResponseDto(Long id, String studentId, String name, Gender gender, String imageLink, Long point, String intro, Role role, String department) {
        this.id = id;
        this.studentId = studentId;
        this.name = name;
        this.gender = gender;
        this.imageLink = imageLink;
        this.point = point;
        this.intro = intro;
        this.role = role;
        this.department = department;
    }

    public static UserInfoResponseDto from(User user) {
        return UserInfoResponseDto.builder()
                .id(user.getId())
                .studentId(user.getStudentId())
                .name(user.getName())
                .gender(user.getGender())
                .imageLink(user.getImageLink())
                .point(user.getPoint())
                .intro(user.getIntro())
                .role(user.getRole())
                .department(user.getDepartment().getName())
                .build();
    }
}

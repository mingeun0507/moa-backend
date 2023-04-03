package com.hanamja.moa.api.dto.user.response;

import com.hanamja.moa.api.entity.department.Department;
import com.hanamja.moa.api.entity.user.Gender;
import com.hanamja.moa.api.entity.user.Role;
import com.hanamja.moa.api.entity.user.User;
import com.hanamja.moa.api.entity.user_department.UserDepartment;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

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
    private List<Department> departments;
    private boolean isNotified;
    private int rank;

    @Builder
    public UserInfoResponseDto(Long id, String studentId, String name, Gender gender, String imageLink, Long point, String intro, Role role, List<Department> departments, boolean isNotified, int rank) {
        this.id = id;
        this.studentId = studentId;
        this.name = name;
        this.gender = gender;
        this.imageLink = imageLink;
        this.point = point;
        this.intro = intro;
        this.role = role;
        this.departments = departments;
        this.isNotified = isNotified;
        this.rank = rank;
    }

    public static UserInfoResponseDto from(User user) {
        List<Department> depts = user.getDepartments().stream().map(UserDepartment::getDepartment).collect(Collectors.toList());
        return UserInfoResponseDto.builder()
                .id(user.getId())
                .studentId(user.getStudentId())
                .name(user.getName())
                .gender(user.getGender())
                .imageLink(user.getImageLink())
                .point(user.getPoint())
                .intro(user.getIntro())
                .role(user.getRole())
                .departments(depts)
                .isNotified(user.getIsNotified())
                .build();
    }

    public static UserInfoResponseDto from(User user, int rank) {
        List<Department> depts = user.getDepartments().stream().map(UserDepartment::getDepartment).collect(Collectors.toList());
        return UserInfoResponseDto.builder()
                .id(user.getId())
                .studentId(user.getStudentId())
                .name(user.getName())
                .gender(user.getGender())
                .imageLink(user.getImageLink())
                .point(user.getPoint())
                .intro(user.getIntro())
                .role(user.getRole())
                .departments(depts)
                .isNotified(user.getIsNotified())
                .rank(rank)
                .build();
    }
}

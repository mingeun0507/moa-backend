package com.hanamja.moa.api.entity.user.UserAccount;

import com.hanamja.moa.api.entity.user.Role;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class UserAccount extends org.springframework.security.core.userdetails.User {

    private Long userId;

    private String studentId;
    private Long departmentId;

    private Role role;

    public UserAccount(Long userId, String studentId, Long departmentId, Role role) {
        super(studentId, "-",new ArrayList<Role>() {{
            add(role);
        }});
        this.userId = userId;
        this.studentId = studentId;
        this.departmentId = departmentId;
        this.role = role;
    }
}

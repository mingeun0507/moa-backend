package com.hanamja.moa.api.entity.user.UserAccount;

import com.hanamja.moa.api.entity.user.Role;
import com.hanamja.moa.api.entity.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Getter
public class UserAccount extends org.springframework.security.core.userdetails.User {

    private Long userId;

    private String studentId;

    private Role role;

    public UserAccount(Long userId, String studentId, Role role) {
        super(studentId, "-",new ArrayList<Role>() {{
            add(role);
        }});
        this.userId = userId;
        this.studentId = studentId;
        this.role = role;
    }
}

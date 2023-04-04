package com.hanamja.moa.api.entity.department;

import com.hanamja.moa.api.entity.user_department.UserDepartment;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "MOA_DEPT")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dept_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "department")
    private List<UserDepartment> userIds;

    @Builder
    public Department(String name) {
        this.name = name;
    }

}

package com.hanamja.moa.api.entity.user;

import com.hanamja.moa.api.entity.album.Album;
import com.hanamja.moa.api.entity.department.Department;
import com.hanamja.moa.api.entity.group.Group;
import com.hanamja.moa.api.entity.user_group.UserGroup;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.Year;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "MOA_USER")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "student_id", nullable = false, unique = true)
    private String studentId;

    @Column(name = "pwd", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "image_link")
    private String imageLink;

    @Column(name = "point")
    private Long point;

    @Column(name = "intro")
    private String intro;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "dept_id", nullable = false)
    private Department department;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private List<Album> albumList;

    @OneToMany(mappedBy = "joiner", fetch = FetchType.LAZY)
    private List<UserGroup> userGroupList;

    @OneToMany(mappedBy = "maker", fetch = FetchType.LAZY)
    private List<Group> groupList;

//    @OneToMany(mappedBy = "metUser", fetch = FetchType.LAZY)
//    private List<Album> metAlbumList;

    @Builder
    public User(String studentId, String password, String name, String gender, String imageLink, Long point, String intro, Role role, Department department) {
        this.studentId = studentId;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.imageLink = imageLink;
        this.point = point;
        this.intro = intro;
        this.role = role;
        this.department = department;
    }

    public boolean isFreshman() {
        String year = Integer.toString(Year.now().getValue());
        return this.studentId.startsWith(year);
    }

    // 온보딩용 업데이트 함수
    public void updateOnboardingInfo(String gender, Department department, String imageLink) {
        this.gender = gender;
        this.department = department;
        this.imageLink = imageLink == null ? this.imageLink : imageLink;
    }

    // 마이페이지 수정용 업데이트 함수
    public void updateUserinfo(String imageLink, String intro) {
        this.imageLink = imageLink == null ? this.imageLink : imageLink;
        this.intro = intro == null ? this.intro : intro;
    }
}

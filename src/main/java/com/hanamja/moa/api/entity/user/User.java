package com.hanamja.moa.api.entity.user;

import com.hanamja.moa.api.entity.album.Album;
import com.hanamja.moa.api.entity.department.Department;
import com.hanamja.moa.api.entity.group.Group;
import com.hanamja.moa.api.entity.point_history.PointHistory;
import com.hanamja.moa.api.entity.user_group.UserGroup;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Year;
import java.util.List;

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
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "image_link")
    private String imageLink;

    @Column(name = "point")
    private Long point;

    @Column(name = "intro")
    private String intro;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_id", nullable = false)
    private Department department;

    @OneToMany(mappedBy = "owner")
    private List<Album> albumList;

    @OneToMany(mappedBy = "joiner")
    private List<UserGroup> userGroupList;

    @OneToMany(mappedBy = "maker")
    private List<Group> groupList;

//    @OneToMany(mappedBy = "metUser")
//    private List<Album> metAlbumList;

    @Column(name = "is_onboarded", nullable = false)
    private boolean isOnboarded;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @OneToMany(mappedBy = "owner")
    private List<PointHistory> pointHistoryList;

    @Builder
    public User(String studentId, String password, String name, Gender gender, String imageLink, Long point, String intro, Role role, Department department, boolean isOnboarded, boolean isActive) {
        this.studentId = studentId;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.imageLink = imageLink;
        this.point = point;
        this.intro = intro;
        this.role = role;
        this.department = department;
        this.isOnboarded = isOnboarded;
        this.isActive = isActive;
    }

    public boolean isFreshman() {
        String year = Integer.toString(Year.now().getValue());
        return this.studentId.startsWith(year);
    }

    // 온보딩용 업데이트 함수
    public void updateOnboardingInfo(Gender gender, Department department, String imageLink) {
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

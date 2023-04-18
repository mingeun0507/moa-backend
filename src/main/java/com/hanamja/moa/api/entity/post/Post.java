package com.hanamja.moa.api.entity.post;

import com.hanamja.moa.api.entity.board_category.BoardCategory;
import com.hanamja.moa.api.entity.department.Department;
import com.hanamja.moa.api.entity.user.User;
import com.hanamja.moa.api.entity.util.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "MOA_POST")
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_id", nullable = false)
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bc_id", nullable = false)
    private BoardCategory boardCategory;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "is_modified", nullable = false)
    private Boolean isModified;

    @Column(name = "thumbnail")
    private String thumbnail; // 예비 컬럼

    @Builder
    public Post(User user, Department department, BoardCategory boardCategory, String title, String content) {
        this.user = user;
        this.department = department;
        this.boardCategory = boardCategory;
        this.title = title;
        this.content = content;
        this.isModified = false;
    }
}

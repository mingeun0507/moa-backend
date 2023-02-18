package com.hanamja.moa.api.entity.user_group;

import com.hanamja.moa.api.entity.group.Group;
import com.hanamja.moa.api.entity.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "MOA_USER_GROUP")
public class UserGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ug_id")
    private Long id;

    @Column(name = "join_at", nullable = false)
    private LocalDateTime joinAt;

    @Column(name = "progress", nullable = false)
    private String progress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User joiner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @Builder
    public UserGroup(String progress, User joiner, Group group) {
        this.joinAt = LocalDateTime.now();
        this.progress = progress;
        this.joiner = joiner;
        this.group = group;
    }
}

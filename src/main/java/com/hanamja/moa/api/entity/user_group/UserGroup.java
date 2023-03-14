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
@Table(name = "MOA_USER_GROUP", indexes = {
        @Index(name = "idx__user_id__group_id", columnList = "user_id, group_id", unique = true),
        @Index(name = "idx__group_id__user_id", columnList = "group_id, user_id", unique = true)
})
public class UserGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ug_id")
    private Long id;

    @Column(name = "join_at", nullable = false)
    private LocalDateTime joinAt;

    @Column(name = "progress", nullable = false)
    private String progress;

    @Column(name = "meeting_img")
    private String meetingImg;

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

    public void updateMeetingImg(String meetingImg){
        this.meetingImg = meetingImg;
    }
}

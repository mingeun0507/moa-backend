package com.hanamja.moa.api.entity.group;

import com.hanamja.moa.api.entity.comment.Comment;
import com.hanamja.moa.api.entity.department.Department;
import com.hanamja.moa.api.entity.group_hashtag.GroupHashtag;
import com.hanamja.moa.api.entity.user.User;
import com.hanamja.moa.api.entity.user_group.UserGroup;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "MOA_GROUP")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "state")
    @Enumerated(value = EnumType.STRING)
    private State state;

    @Column(name = "max_num", nullable = false)
    private Long maxPeopleNum;

    @Column(name = "current_num", nullable = false)
    private Long currentPeopleNum;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "modified_at", nullable = false)
    private LocalDateTime modifiedAt;

    @Column(name = "meeting_at")
    private LocalDateTime meetingAt;

    @Column(name = "image_link")
    private String imageLink;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User maker;

    @OneToMany(mappedBy = "group")
    private List<UserGroup> userGroupList;

    @OneToMany(mappedBy = "group")
    private List<GroupHashtag> groupHashtagList;

    @OneToMany(mappedBy = "group")
    private List<Comment> commentList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_id", nullable = false)
    private Department department;

    @Builder
    public Group(String name, String description, Long maxPeopleNum, Long currentPeopleNum, LocalDateTime meetingAt, String imageLink, User maker, Department department) {
        this.name = name;
        this.description = description;
        this.state = State.RECRUITING;
        this.maxPeopleNum = maxPeopleNum;
        this.currentPeopleNum = currentPeopleNum;
        this.createdAt = LocalDateTime.now();
        this.modifiedAt = LocalDateTime.now();
        this.meetingAt = meetingAt;
        this.imageLink = imageLink;
        this.maker = maker;
        this.department = department;
    }

    // 인증샷에 대한 update 메소드
    public void uploadImage(String imageLink) {
        this.imageLink = imageLink;
    }

    // 모임 정보 수정에 대한 update 메소드
    public void modifyGroupInfo(String name, String description, LocalDateTime meetingAt, Long maxPeopleNum) {
        if (Optional.ofNullable(meetingAt).isPresent()){
            if (Optional.ofNullable(this.meetingAt).isPresent() && !this.meetingAt.equals(meetingAt)){
                // 수정사항없이 보냈을 때 +9 안되도록 조건문 추가
                meetingAt = ZonedDateTime.of(meetingAt, ZoneId.of("Asia/Seoul"))
                        .toLocalDateTime().plusHours(9L);
            } else if(Optional.ofNullable(this.meetingAt).isEmpty()){
                // 만나는 날짜 미정일 경우 NPE 해결
                meetingAt = ZonedDateTime.of(meetingAt, ZoneId.of("Asia/Seoul"))
                        .toLocalDateTime().plusHours(9L);
            }
        }
        this.name = name;
        this.description = description;
        this.modifiedAt = LocalDateTime.now();
        this.meetingAt = meetingAt;
        this.maxPeopleNum = maxPeopleNum;
    }


    public void addCurrentPeopleNum() {
        this.currentPeopleNum++;
        // 참여했을 떄 인원수 늘려주는 메소드
    }

    public void subtractCurrentPeopleNum() {
        this.currentPeopleNum--;
        // 탈퇴했을 때 인원수 줄여주는 메소드
    }

    public void updateCurrentPeopleNum(Long currNum) {
        this.currentPeopleNum = currNum;
    }

    public void updateNullMeetingAt(LocalDateTime now) {
        this.meetingAt = now;
    }

    public Boolean isFull() {
        return Objects.equals(this.currentPeopleNum, this.maxPeopleNum);
        // 인원수 늘리기 전에 체크하는 메소드
    }

    public void updateState(State newState) {
        this.state = newState;
        // 모임 상태 변경 시 사용할 메소드
    }

    public void setCurrentPeopleNum(Long size) {
        this.currentPeopleNum = size;
    }

    public int getCommentCount() {
        return commentList == null ? 0 : this.commentList.size();
    }
}

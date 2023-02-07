package com.hanamja.moa.api.entity.group;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "MOA_GROUP")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "max_num", nullable = false)
    private Long maxPeopleNum;

    @Column(name = "current_num", nullable = false)
    private Long currentPeopleNum;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "meeting_at", nullable = false)
    private LocalDateTime meetingAt;

    @Column(name = "image_link")
    private String imageLink;

    @Builder
    public Group(String name, String description, Long maxPeopleNum, Long currentPeopleNum, LocalDateTime createdAt, LocalDateTime meetingAt, String imageLink) {
        this.name = name;
        this.description = description;
        this.maxPeopleNum = maxPeopleNum;
        this.currentPeopleNum = currentPeopleNum;
        this.createdAt = createdAt;
        this.meetingAt = meetingAt;
        this.imageLink = imageLink;
    }
}

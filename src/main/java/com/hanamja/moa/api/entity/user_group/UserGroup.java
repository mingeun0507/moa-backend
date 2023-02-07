package com.hanamja.moa.api.entity.user_group;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "MOA_JOIN")
public class UserGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO: 레퍼런스 키, 생성자 추가 필요

    @Column(name = "join_at", nullable = false)
    private LocalDateTime joinAt;

    @Column(name = "progress", nullable = false)
    private String progress;

}

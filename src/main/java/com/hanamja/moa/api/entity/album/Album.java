package com.hanamja.moa.api.entity.album;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "MOA_ALBUm")
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_badged", nullable = false)
    private Boolean isBadged;

    // TODO: 레퍼런스 키 추가 필요
}

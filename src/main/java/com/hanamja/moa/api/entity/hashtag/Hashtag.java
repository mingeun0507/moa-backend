package com.hanamja.moa.api.entity.hashtag;

import com.hanamja.moa.api.entity.group_hashtag.GroupHashtag;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "MOA_HASHTAG")
public class Hashtag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hashtag_id")
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "touched_at", nullable = false)
    private LocalDateTime touchedAt;

    @OneToMany(mappedBy = "hashtag", fetch = FetchType.LAZY)
    private List<GroupHashtag> groupHashtagList;

    @Builder
    public Hashtag(String name) {
        this.name = name;
        this.touchedAt = LocalDateTime.now();
    }

    public void updateTouchedAt() {
        this.touchedAt = LocalDateTime.now();
    }
}

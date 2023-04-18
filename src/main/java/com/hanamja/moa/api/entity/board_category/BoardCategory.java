package com.hanamja.moa.api.entity.board_category;

import com.hanamja.moa.api.entity.post.Post;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "MOA_BOARD_CATEGORY")
public class BoardCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bc_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Post post;

    @Column(name = "name", nullable = false)
    private String name;

    @Builder
    public BoardCategory(Post post, String name) {
        this.post = post;
        this.name = name;
    }
}

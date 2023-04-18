package com.hanamja.moa.api.entity.post_bookmark;

import com.hanamja.moa.api.entity.post.Post;
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
@Table(name = "MOA_POST_BOOKMARK")
public class PostBookmark extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pb_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public PostBookmark(Post post, User user) {
        this.post = post;
        this.user = user;
    }
}

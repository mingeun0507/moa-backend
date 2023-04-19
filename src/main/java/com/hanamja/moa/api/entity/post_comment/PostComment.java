package com.hanamja.moa.api.entity.post_comment;

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
@Table(name = "MOA_POST_COMMENT")
public class PostComment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pc_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "is_modified", nullable = false)
    private boolean isModified;

    @Column(name = "is_deleted")
    private boolean isDeleted; // 예비 컬럼

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_cm_id")
    private PostComment parentComment;

    @Column(name = "is_reply")
    private boolean isReply;

    @Column(name = "comment_order")
    private Integer commentOrder;

    @Builder
    public PostComment(Post post, User user, String content, PostComment parentComment, boolean isReply, Integer commentOrder) {
        this.post = post;
        this.user = user;
        this.content = content;
        this.isModified = false;
        this.isDeleted = false;
        this.parentComment = parentComment;
        this.isReply = isReply;
        this.commentOrder = commentOrder;
    }
}

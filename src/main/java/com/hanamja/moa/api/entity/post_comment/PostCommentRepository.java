package com.hanamja.moa.api.entity.post_comment;

import com.hanamja.moa.api.entity.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
    void deleteAllByPost(Post post);

    Optional<PostComment> findById(Long id);
}

package com.hanamja.moa.api.entity.post_like;

import com.hanamja.moa.api.entity.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    void deleteAllByPost(Post post);
    Optional<PostLike> findByPost_IdAndUser_Id(Long postId, Long userId);
}

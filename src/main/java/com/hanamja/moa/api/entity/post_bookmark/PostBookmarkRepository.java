package com.hanamja.moa.api.entity.post_bookmark;

import com.hanamja.moa.api.entity.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostBookmarkRepository extends JpaRepository<PostBookmark, Long> {
    void deleteAllByPost(Post post);
    Optional<PostBookmark> findByPost_IdAndUser_Id(Long postId, Long userId);
}

package com.hanamja.moa.api.entity.post_comment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostCommentRepository extends JpaRepository<PostComment, Long> {

    Optional<PostComment> findById(Long id);
}

package com.hanamja.moa.api.entity.post_image;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {
    void deleteAllByPost_Id(Long postId);
}

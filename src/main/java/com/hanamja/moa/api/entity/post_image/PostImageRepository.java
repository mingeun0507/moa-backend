package com.hanamja.moa.api.entity.post_image;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostImageRepository extends JpaRepository<PostImage, Long>, PostImageRepositoryCustom {
    void deleteAllByPost_Id(Long postId);

    Optional<PostImage> findByImage(String imageUrl);

    List<PostImage> findAllExistByImageUrl(List<String> imageUrl);
}

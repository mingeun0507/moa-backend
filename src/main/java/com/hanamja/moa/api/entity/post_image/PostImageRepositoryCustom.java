package com.hanamja.moa.api.entity.post_image;

import java.util.List;

public interface PostImageRepositoryCustom {
    List<PostImage> findAllExistByImageUrl(List<String> imageUrl);
}

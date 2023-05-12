package com.hanamja.moa.api.entity.post_image;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.hanamja.moa.api.entity.post_image.QPostImage.postImage;

@RequiredArgsConstructor
@Repository
public class PostImageRepositoryCustomImpl implements PostImageRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<PostImage> findAllExistByImageUrl(List<String> imageUrl) {
        return jpaQueryFactory.selectFrom(postImage)
                .where(postImage.image.notIn(imageUrl))
                .fetch();
    }

}

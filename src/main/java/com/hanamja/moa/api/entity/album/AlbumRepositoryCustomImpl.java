package com.hanamja.moa.api.entity.album;

import com.hanamja.moa.api.controller.album.SortedBy;
import com.hanamja.moa.api.entity.user.User;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.hanamja.moa.api.entity.album.QAlbum.album;
import static com.hanamja.moa.api.entity.user.QUser.user;

@Repository
@RequiredArgsConstructor
public class AlbumRepositoryCustomImpl implements AlbumRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    public List<User> findAllAlbumUserSorted(Long userId, SortedBy sortedBy){
        OrderSpecifier<?> specifier = sortedBy.equals(SortedBy.RECENT) ?
                            album.updatedAt.desc() : album.metUser.name.asc();

        return jpaQueryFactory.select(album.metUser).from(album)
                .join(album.metUser, user)
                .where(album.owner.id.eq(userId))
                .orderBy(specifier)
                .fetch();
    }
}

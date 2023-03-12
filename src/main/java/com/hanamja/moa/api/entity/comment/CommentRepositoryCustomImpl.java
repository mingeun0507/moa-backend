package com.hanamja.moa.api.entity.comment;

import com.hanamja.moa.api.entity.group.Group;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.hanamja.moa.api.entity.comment.QComment.comment;

@RequiredArgsConstructor
@Repository
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Comment> findAllByGroupAndIdGreaterThanEqual(Group group, Long cursor, Pageable pageable) {



        List<Comment> commentList = jpaQueryFactory.selectFrom(comment)
                                                        .where(comment.group.eq(group))
                                                        .where(comment.id.loe(cursor))
                                                        .orderBy(comment.id.desc())
                                                        .offset((long) (pageable.getPageNumber()) * pageable.getPageSize())
                                                        .limit(pageable.getPageSize())
                                                    .fetch();

        Long count = jpaQueryFactory.select(comment.count())
                                            .from(comment)
                                            .where(comment.group.eq(group))
                                            .where(comment.id.loe((cursor)))
                                        .fetchOne();

        return new PageImpl<>(commentList, pageable, count);

    }
}

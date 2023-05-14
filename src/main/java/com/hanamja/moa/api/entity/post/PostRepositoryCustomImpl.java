package com.hanamja.moa.api.entity.post;

import com.hanamja.moa.api.dto.post.response.PostInfoResponseDto;
import com.hanamja.moa.api.entity.board.Board;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.hanamja.moa.api.entity.board_category.QBoardCategory.boardCategory;
import static com.hanamja.moa.api.entity.post.QPost.post;
import static com.hanamja.moa.api.entity.post_comment.QPostComment.postComment;
import static com.hanamja.moa.api.entity.post_like.QPostLike.postLike;
import static com.hanamja.moa.api.entity.user.QUser.user;


@RequiredArgsConstructor
@Repository
public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Slice<PostInfoResponseDto> findAllPagedPostByCategoryId(Board targetBoard, Long cursor, Pageable pageable, Long categoryId) {

        List<PostInfoResponseDto> results = jpaQueryFactory
                .select(
                        post.id,
                        post.title,
                        post.content,
                        post.thumbnail,
                        post.createdAt,
                        user.id,
                        user.name,
                        user.imageLink,
                        boardCategory.id,
                        boardCategory.name,
                        postLike.count(),
                        postComment.count()
                )
                .from(post)
                .join(user).on(post.user.id.eq(user.id))
                .join(boardCategory).on(post.boardCategory.id.eq(boardCategory.id))
                .leftJoin(postLike).on(post.id.eq(postLike.post.id))
                .leftJoin(postComment).on(post.id.eq(postComment.post.id))
                .where(
                        boardCategory.board.id.eq(targetBoard.getId()),
                        ltPostId(cursor),
                        eqCategoryFilter(categoryId)
                )
                .groupBy(post.id, post.title, post.content, post.thumbnail, post.createdAt, user.id, user.name, user.imageLink, boardCategory.id, boardCategory.name)
                .orderBy(post.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch()
                .stream()
                .map(row -> PostInfoResponseDto.from(
                        row.get(post.id),
                        row.get(post.title),
                        row.get(post.content),
                        row.get(post.thumbnail),
                        row.get(post.createdAt),
                        row.get(user.id),
                        row.get(user.name),
                        row.get(user.imageLink),
                        row.get(boardCategory.id),
                        row.get(boardCategory.name),
                        row.get(postLike.count().intValue()),
                        row.get(postComment.count().intValue())))
                .collect(Collectors.toList());

        boolean hasNext = results.size() > pageable.getPageSize();
        if (hasNext) {
            results.remove(results.size() - 1);
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }

    @Override
    public Slice<PostInfoResponseDto> searchAllPagedPostByTitleOrComment(Board targetBoard, String keyword, Long cursor, Pageable pageable, Long categoryId) {


        List<PostInfoResponseDto> results = jpaQueryFactory
                .select(
                        post.id,
                        post.title,
                        post.content,
                        post.thumbnail,
                        post.createdAt,
                        user.id,
                        user.name,
                        user.imageLink,
                        boardCategory.id,
                        boardCategory.name,
                        postLike.count(),
                        postComment.count()
                )
                .from(post)
                .join(user).on(post.user.id.eq(user.id))
                .join(boardCategory).on(post.boardCategory.id.eq(boardCategory.id))
                .leftJoin(postLike).on(post.id.eq(postLike.post.id))
                .leftJoin(postComment).on(post.id.eq(postComment.post.id))
                .where(
                        boardCategory.board.id.eq(targetBoard.getId()),
                        ltPostId(cursor),
                        (titleContainKeyword(keyword).or(contentContainKeyword(keyword))),
                        eqCategoryFilter(categoryId)
                )
                .groupBy(post.id, post.title, post.content, post.thumbnail, post.createdAt, user.id, user.name, user.imageLink, boardCategory.id, boardCategory.name)
                .orderBy(post.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch()
                .stream()
                .map(row -> PostInfoResponseDto.from(
                        row.get(post.id),
                        row.get(post.title),
                        row.get(post.content),
                        row.get(post.thumbnail),
                        row.get(post.createdAt),
                        row.get(user.id),
                        row.get(user.name),
                        row.get(user.imageLink),
                        row.get(boardCategory.id),
                        row.get(boardCategory.name),
                        row.get(postLike.count().intValue()),
                        row.get(postComment.count().intValue())))
                .collect(Collectors.toList());

        boolean hasNext = results.size() > pageable.getPageSize();
        if (hasNext) {
            results.remove(results.size() - 1);
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }

    private BooleanExpression ltPostId(Long cursorId) {
        return cursorId == null ? null : post.id.lt(cursorId);
    }

    private BooleanExpression titleContainKeyword(String keyword) {
        return keyword == null ? null : post.title.containsIgnoreCase(keyword);
    }

    private BooleanExpression contentContainKeyword(String keyword) {
        return keyword == null ? null : post.content.containsIgnoreCase(keyword);
    }

    private BooleanExpression eqCategoryFilter(Long categoryId) {
        if (categoryId == null) {
            return Expressions.asBoolean(true).isTrue();
        }
        return boardCategory.id.eq(categoryId);
    }

}

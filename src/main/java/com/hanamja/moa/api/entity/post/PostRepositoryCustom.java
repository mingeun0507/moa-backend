package com.hanamja.moa.api.entity.post;

import com.hanamja.moa.api.dto.post.response.PostDetailInfoResponseDto;
import com.hanamja.moa.api.dto.post.response.PostInfoResponseDto;
import com.hanamja.moa.api.entity.board.Board;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface PostRepositoryCustom {
    Slice<PostInfoResponseDto> findAllPagedPostByCategoryId(Board board, Long cursor, Pageable pageable, Long categoryId);

    Slice<PostInfoResponseDto> searchAllPagedPostByTitleOrComment(Board board, String keyword, Long cursor, Pageable pageable, Long categoryId);

    PostDetailInfoResponseDto findPostDetailInfoById(Long postId, Long userId);
}

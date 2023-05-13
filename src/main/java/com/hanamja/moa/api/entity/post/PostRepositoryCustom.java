package com.hanamja.moa.api.entity.post;

import com.hanamja.moa.api.dto.post.response.PostInfoResponseDto;
import com.hanamja.moa.api.entity.board.Board;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface PostRepositoryCustom {
    Slice<PostInfoResponseDto> findAllSimplePostInfo(Board board, Long cursor, Pageable pageable);
}

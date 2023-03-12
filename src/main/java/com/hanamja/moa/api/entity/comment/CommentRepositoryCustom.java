package com.hanamja.moa.api.entity.comment;

import com.hanamja.moa.api.entity.group.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentRepositoryCustom {

    Page<Comment> findAllByGroupAndIdGreaterThanEqual(Group group, Long cursor, Pageable pageable);
}

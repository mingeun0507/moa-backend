package com.hanamja.moa.api.entity.comment;

import com.hanamja.moa.api.entity.group.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {
    Optional<Comment> findTopByGroupOrderByIdDesc(Group group);

    void deleteAllByGroup_Id(Long groupId);

    Page<Comment> findAllByGroupAndIdGreaterThanEqual(Group group, Long cursor, Pageable pageable);
}

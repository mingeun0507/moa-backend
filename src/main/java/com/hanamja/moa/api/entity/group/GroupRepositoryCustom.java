package com.hanamja.moa.api.entity.group;

import com.hanamja.moa.api.controller.album.SortedBy;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GroupRepositoryCustom {
    List<Group> findAllByUserId(Long userId);
    List<Group> findAllByPageAndSort(SortedBy sortedBy, Long cursorId, Pageable pageable);
}

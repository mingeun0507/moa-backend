package com.hanamja.moa.api.entity.group;

import com.hanamja.moa.api.controller.group.SortedBy;
import com.hanamja.moa.api.entity.user.UserAccount.UserAccount;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface GroupRepositoryCustom {
    List<Group> findAllByUserId(Long userId);
    List<Group> findAllByPageAndSort(UserAccount userAccount, LocalDateTime now, SortedBy sortedBy, Long cursorId, Pageable pageable);
}

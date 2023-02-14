package com.hanamja.moa.api.entity.group;

import java.util.List;

public interface GroupRepositoryCustom {
    List<Group> findAllByUserId(Long userId);
}

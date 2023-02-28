package com.hanamja.moa.api.entity.user_group;

import com.hanamja.moa.api.controller.SortedBy;
import com.hanamja.moa.api.entity.group.State;
import com.hanamja.moa.api.entity.user.User;

import java.util.List;

public interface UserGroupRepositoryCustom {
    List<User> findMyAlbumUser(Long userId, State state, SortedBy sortedBy);
}

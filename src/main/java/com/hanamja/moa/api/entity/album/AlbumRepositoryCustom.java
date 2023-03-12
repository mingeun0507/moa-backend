package com.hanamja.moa.api.entity.album;

import com.hanamja.moa.api.controller.album.SortedBy;
import com.hanamja.moa.api.entity.user.User;

import java.util.List;

public interface AlbumRepositoryCustom {
    List<User> findAllAlbumUserSorted(Long userId, SortedBy sortedBy);
}

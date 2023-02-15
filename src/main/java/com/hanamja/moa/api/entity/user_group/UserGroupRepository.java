package com.hanamja.moa.api.entity.user_group;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {
    void deleteAllByGroup_Id(Long groupId);

    List<UserGroup> findAllByGroup_Id(Long groupId);

    int countAllByJoiner_Id(Long userId);
}

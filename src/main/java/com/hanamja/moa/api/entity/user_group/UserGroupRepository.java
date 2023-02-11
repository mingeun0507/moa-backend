package com.hanamja.moa.api.entity.user_group;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {
    void deleteAllByGroup_Id(Long groupId);
}

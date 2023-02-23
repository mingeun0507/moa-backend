package com.hanamja.moa.api.entity.user_group;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {
    void deleteAllByGroup_Id(Long groupId);

    void deleteUserGroupByGroup_IdAndJoiner_Id(Long gid, Long joinerId);

    List<UserGroup> findAllByGroup_Id(Long groupId);

    int countAllByJoiner_Id(Long userId);
    
    Optional<UserGroup> findByGroupIdAndJoinerId(Long groupId, Long joinerId);

    List<UserGroup> findAllByJoiner_IdAndProgress(Long uid, String progress);
    
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE UserGroup ug SET ug.progress = :state WHERE ug.group.id = :gid")
    void updateProgress(@Param(value = "state") String state,
                        @Param(value = "gid") Long gid);
}

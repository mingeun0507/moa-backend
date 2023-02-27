package com.hanamja.moa.api.entity.user_group;

import com.hanamja.moa.api.entity.group.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {
    void deleteAllByGroup_Id(Long groupId);

    void deleteUserGroupByGroup_IdAndJoiner_Id(Long gid, Long joinerId);

    @Query(value = "SELECT ug FROM UserGroup ug JOIN FETCH ug.joiner WHERE ug.group.id = :gid")
    List<UserGroup> findAllByGroup_Id(@Param(value = "gid") Long gid);

    int countAllByJoiner_IdAndGroup_State(Long userId, State state);

    Optional<UserGroup> findByGroupIdAndJoinerId(Long groupId, Long joinerId);

    @Query(value = "SELECT ug FROM UserGroup ug WHERE ug.joiner.id = :uid AND ug.group.state = :state")
    List<UserGroup> findAllDoneGroupByUserId(@Param(value = "uid") Long uid, @Param(value = "state") State state);

    @Query(value = "SELECT uug FROM UserGroup uug WHERE uug.joiner.id <> :uid " +
            "AND uug.group.id in (SELECT ug.group.id FROM UserGroup ug WHERE ug.joiner.id = :uid AND ug.group.state = :state)")
    List<UserGroup> findAllDoneGroupJoinUserId(@Param(value = "uid") Long uid, @Param(value = "state") State state);

    @Query(value = "SELECT uug FROM UserGroup uug WHERE uug.joiner.id <> :uid and uug.joiner.id = :jid " +
            "AND uug.group.id in (SELECT ug.group.id FROM UserGroup ug WHERE ug.joiner.id = :uid AND ug.group.state = :state)")
    List<UserGroup> findOnePersonCard(@Param(value = "uid") Long uid, @Param(value = "jid") Long jid, @Param(value = "state") State state);

    List<UserGroup> findAllByJoiner_IdAndProgress(Long uid, String progress);
    
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE UserGroup ug SET ug.meetingImg = :img WHERE ug.group.id = :gid AND ug.joiner.id = :jid")
    void updateFrontCardImg(@Param(value = "img") String img,
                        @Param(value = "gid") Long gid, @Param(value = "jid") Long jid);

    boolean existsByGroupIdAndJoinerId(Long groupId, Long userId);
}

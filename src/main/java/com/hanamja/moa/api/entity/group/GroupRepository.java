package com.hanamja.moa.api.entity.group;


import com.hanamja.moa.api.controller.group.SortedBy;
import com.hanamja.moa.api.entity.user.UserAccount.UserAccount;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long>, GroupRepositoryCustom {

    Optional<Group> findById(Long id);

    @Query(value = "SELECT g FROM Group g JOIN FETCH UserGroup ug ON g.id = ug.group.id WHERE g.id = :gid")
    Group findGroupByGid(@Param(value = "gid")Long gid);

    List<Group> findAllByUserId(Long userId);

    boolean existsByIdAndMaker_Id(Long gid, Long uid);

    @Modifying(clearAutomatically = true)// 모임이 끝나면 인증샷 등록
    @Query(value = "UPDATE Group gp SET gp.imageLink = :imageLink, gp.state = :state, gp.modifiedAt = :now WHERE gp.id = :gid")
    void updateCompleteGroup(@Param(value = "imageLink") String imageLink, @Param(value = "now") LocalDateTime now,
                             @Param(value = "gid") Long gid, @Param(value = "state") State state);

    List<Group> findAllByIdAndState(Long gid, State state);

    @Query(value = "SELECT g FROM Group g " +
            "WHERE g.state = :state AND COALESCE(g.meetingAt, :now+:now) > :now " +
            "ORDER BY g.createdAt DESC")
    List<Group> findExistingGroupsByRECENT(@Param(value = "state") State state, @Param(value = "now") LocalDateTime now);
    List<Group> findAllByStateAndMeetingAtAfterOrderByCreatedAtDesc(State state, LocalDateTime currentTime);

    List<Group> findAllByStateAndMeetingAtAfterOrderByMeetingAtAscCreatedAtDesc(State state, LocalDateTime currentTime);
    List<Group> findAllByStateAndDepartment_IdAndMeetingAtAfterOrderByMeetingAtAscCreatedAtDesc(State state, Long departmentId, LocalDateTime currentTime);
    List<Group> findAllByStateAndMeetingAtBeforeOrderByCreatedAtDesc(State state, LocalDateTime currentTime);

    List<Group> findAllByStateAndMeetingAtOrderByCreatedAtDesc(State state, LocalDateTime currentTime);


    // TODO 아래 두 함수 하나로 합칠것
    @Query(value = "SELECT distinct g FROM Group g LEFT JOIN FETCH GroupHashtag gh ON g.id = gh.group.id LEFT JOIN FETCH Hashtag h ON gh.hashtag.id = h.id WHERE (g.name LIKE %:keyword% OR h.name LIKE %:keyword%) AND g.department.id = :deptId AND g.state = 'RECRUITING' AND (g.meetingAt > now() OR g.meetingAt is Null) ORDER BY g.createdAt DESC")
    List<Group> searchGroupByKeyword(@Param(value = "keyword") String keyword, @Param(value = "deptId") Long deptId);

    @Query(value = "SELECT distinct g FROM Group g LEFT JOIN FETCH GroupHashtag gh ON g.id = gh.group.id LEFT JOIN FETCH Hashtag h ON gh.hashtag.id = h.id WHERE (g.name LIKE %:keyword% OR h.name LIKE %:keyword%) AND g.department.id = :deptId AND g.state = 'RECRUITING' AND (g.meetingAt > now() OR g.meetingAt is Null) ORDER BY g.meetingAt asc nulls last, g.createdAt DESC")
    List<Group> searchGroupByMeetingAtAndKeyword(@Param(value = "keyword") String keyword, @Param(value = "deptId") Long deptId);


    List<Group> findAllByStateAndNameContainsOrderByCreatedAtDesc(State state, String name);

    List<Group> findAllByMaker_IdAndDepartment_Id(Long userId, Long departmentId);

    void deleteById(Long gid);

    @Modifying(clearAutomatically = true)// 모임이 끝나면 인증샷 등록
    @Query(value = "UPDATE Group gp SET gp.state = :state WHERE gp.id = :gid")
    void updateGroupState(@Param(value = "state") State state, @Param(value = "gid") Long gid);

    @Query(value = "SELECT g FROM Group AS g JOIN FETCH UserGroup AS ug ON g.id = ug.group.id " +
            "WHERE ug.joiner.id = :uid AND g.state = :state AND g.department.id = :deptId " +
            "ORDER BY g.createdAt DESC")
    List<Group> findAllJoinGroupByUserId(@Param(value = "uid") Long uid, @Param(value = "state") State state, @Param(value = "deptId") Long deptId);

    List<Group> findAllByPageAndSort(UserAccount userAccount, LocalDateTime now, SortedBy sortedBy, Long cursorId, Pageable pageable);

}

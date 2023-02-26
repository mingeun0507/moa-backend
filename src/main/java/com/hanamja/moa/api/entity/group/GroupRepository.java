package com.hanamja.moa.api.entity.group;


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
    @Query(value = "UPDATE Group gp SET gp.imageLink = :imageLink, gp.state = :state WHERE gp.id = :gid")
    void updateCompleteGroup(@Param(value = "imageLink") String imageLink,
                             @Param(value = "gid") Long gid, @Param(value = "state") State state);

    List<Group> findAllByIdAndState(Long gid, State state);

    List<Group> findAllByStateAndMeetingAtAfterOrderByCreatedAtDesc(State state, LocalDateTime currentTime);

    List<Group> findAllByStateAndMeetingAtAfterOrderByMeetingAtAscCreatedAtDesc(State state, LocalDateTime currentTime);

    List<Group> findAllByStateAndMeetingAtOrderByCreatedAtDesc(State state, LocalDateTime currentTime);


    @Query(value = "select distinct *\n" +
            "from moa_group mg, moa_group_hashtag mgh, moa_hashtag mh\n" +
            "where mg.name like :keyword or mh.name like :keyword and mg.group_id = mgh.group_id and mgh.hashtag_id = mh.hashtag_id\n" +
            "order by mg.created_at desc", nativeQuery = true)
    List<Group> searchGroupByKeyword(@Param(value = "keyword") String keyword);

    List<Group> findAllByStateAndNameContainsOrderByCreatedAtDesc(State state, String name);

    List<Group> findAllByMaker_Id(Long userId);

    void deleteById(Long gid);

    @Modifying(clearAutomatically = true)// 모임이 끝나면 인증샷 등록
    @Query(value = "UPDATE Group gp SET gp.state = :state WHERE gp.id = :gid")
    void updateGroupState(@Param(value = "state") State state, @Param(value = "gid") Long gid);

    @Query(value = "SELECT g FROM Group AS g JOIN FETCH UserGroup AS ug ON g.id = ug.group.id " +
            "WHERE ug.joiner.id = :uid AND g.state = :state " +
            "ORDER BY g.createdAt DESC")
    List<Group> findAllJoinGroupByUserId(@Param(value = "uid") Long uid, @Param(value = "state") State state);
}

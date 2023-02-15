package com.hanamja.moa.api.entity.group;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long>, GroupRepositoryCustom {

    Optional<Group> findById(Long id);

    List<Group> findAllByUserId(Long userId);
    boolean existsByIdAndMaker_Id(Long gid, Long uid);

    @Modifying(clearAutomatically = true)// 모임이 끝나면 인증샷 등록
    @Query(value = "UPDATE Group gp SET gp.imageLink = :imageLink WHERE gp.id = :gid")
    void updateGroupImage(@Param(value = "imageLink")String imageLink,
                          @Param(value = "gid")Long gid);

}

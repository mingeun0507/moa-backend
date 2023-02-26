package com.hanamja.moa.api.entity.album;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AlbumRepository extends JpaRepository<Album, Long> {
    Boolean existsByOwner_IdAndMetUser_Id(Long ownerId, Long metUserId);
    boolean existsByMetUser_Id(Long uid);

    @Query(value = "SELECT a FROM Album a JOIN FETCH a.owner WHERE a.owner.id = :oid")
    List<Album> findAllByOwner_Id(Long oid);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Album a SET a.isBadged = :state WHERE a.metUser.id = :mid AND a.owner.id = :oid")
    void updateBadgeState(@Param(value = "state")boolean state,
                          @Param(value = "mid")Long mid,
                          @Param(value = "oid")Long oid);
}

package com.hanamja.moa.api.entity.album;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumRepository extends JpaRepository<Album, Long> {
    Boolean existsByOwner_IdAndMetUser_Id(Long ownerId, Long metUserId);
}

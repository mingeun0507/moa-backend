package com.hanamja.moa.api.entity.group_hashtag;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupHashtagRepository extends JpaRepository<GroupHashtag, Long> {

    Optional<GroupHashtag> findAllByGroup_Id(Long groupId);

    void deleteAllByGroup_Id(Long groupId);
}

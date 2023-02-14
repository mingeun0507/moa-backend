package com.hanamja.moa.api.entity.group_hashtag;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupHashtagRepository extends JpaRepository<GroupHashtag, Long> {

    List<GroupHashtag> findAllByGroup_Id(Long groupId);

    void deleteAllByGroup_Id(Long groupId);
}

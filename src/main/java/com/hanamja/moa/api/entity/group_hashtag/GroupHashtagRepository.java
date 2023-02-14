package com.hanamja.moa.api.entity.group_hashtag;


import com.hanamja.moa.api.entity.group.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupHashtagRepository extends JpaRepository<GroupHashtag, Long> {
    void deleteAllByGroup_Id(Long groupId);
}

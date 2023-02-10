package com.hanamja.moa.api.entity.group_hashtag;


import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupHashtagRepository extends JpaRepository<GroupHashtag, Long> {
    void deleteAllByGroup_Id(Long groupId);
}

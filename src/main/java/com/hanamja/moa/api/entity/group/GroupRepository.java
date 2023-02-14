package com.hanamja.moa.api.entity.group;


import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findAllByStateOrderByCreatedAtDesc(State state);

    List<Group> findAllByStateAndMeetingAtAfterOrderByMeetingAtAscCreatedAtDesc(State state, LocalDateTime currentTime);
}

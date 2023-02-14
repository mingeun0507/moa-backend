package com.hanamja.moa.api.entity.group;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long>, GroupRepositoryCustom {

    Optional<Group> findById(Long id);

    List<Group> findAllByUserId(Long userId);
}

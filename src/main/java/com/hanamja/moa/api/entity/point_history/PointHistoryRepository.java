package com.hanamja.moa.api.entity.point_history;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {
    List<PointHistory> findAllByOwner_Id(Long ownerId);
}

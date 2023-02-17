package com.hanamja.moa.api.entity.history;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Long> {
    List<History> findAllByOwner_Id(Long ownerId);
}

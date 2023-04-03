package com.hanamja.moa.api.entity.user_department;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserDepartmentRepository extends JpaRepository<UserDepartment, Long> {
    List<UserDepartment> findAllByUserId(Long userId);
}

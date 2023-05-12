package com.hanamja.moa.api.entity.user_department;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserDepartmentRepository extends JpaRepository<UserDepartment, Long> {
    @Query(value = "SELECT ud FROM UserDepartment ud WHERE ud.user.id = :userId")
    List<UserDepartment> findByUser_Id(@Param(value = "userId") Long userId);
}

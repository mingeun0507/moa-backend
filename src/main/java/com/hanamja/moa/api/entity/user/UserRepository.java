package com.hanamja.moa.api.entity.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserById(Long uid);

    Optional<User> findByStudentId(String studentId);

    Boolean existsByStudentId(String studentId);

    List<User> findAllByOrderByPointDesc();

    List<User> findTop20ByRoleOrderByPointDesc(Role role);
}

package com.hanamja.moa.api.entity.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserById(Long uid);

    Optional<User> findByStudentId(String studentId);

    Boolean existsByStudentId(String studentId);

    List<User> findTop20ByRoleOrderByPointDesc(Role role);

    @Query(value = "SELECT COUNT(u) FROM User u " +
            "WHERE u.point >= (SELECT uu.point FROM User uu WHERE uu.id = :uid AND uu.role = :role) AND u.role = :role")
    int getUserRank(@Param(value = "uid") Long uid, @Param(value = "role")Role role);

}

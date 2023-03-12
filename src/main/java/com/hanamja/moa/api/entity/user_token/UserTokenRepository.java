package com.hanamja.moa.api.entity.user_token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserTokenRepository extends JpaRepository<UserToken, Long> {

    Optional<UserToken> findByRefreshToken(String refreshToken);

    boolean existsByUser_Id(Long uid);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE UserToken ut SET ut.refreshToken = :token WHERE ut.user.id = :uid")
    void updateRefreshToken(@Param(value = "uid") Long uid, @Param(value = "token") String token);
}

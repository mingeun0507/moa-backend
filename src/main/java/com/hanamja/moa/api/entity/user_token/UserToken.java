package com.hanamja.moa.api.entity.user_token;

import com.hanamja.moa.api.entity.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "MOA_USER_TOKEN")
public class UserToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_token_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

    @Builder
    public UserToken(User user, String refreshToken) {
        this.user = user;
        this.refreshToken = refreshToken;
    }
}

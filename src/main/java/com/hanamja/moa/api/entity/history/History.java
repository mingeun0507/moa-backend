package com.hanamja.moa.api.entity.history;

import com.hanamja.moa.api.entity.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "MOA_HISTORY")
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "msg")
    private String message;

    @Column(name = "point", nullable = false)
    private Long point;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Builder
    public History(String title, String message, Long point, User owner) {
        this.title = title;
        this.message = message;
        this.point = point;
        this.createdAt = LocalDateTime.now();
        this.owner = owner;
    }
}

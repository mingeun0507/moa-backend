package com.hanamja.moa.api.entity.notification;

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
@Table(name = "MOA_NOTIFICATION")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @Column(name = "content")
    private String content;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "reason")
    private String reason;

    @Column(name = "is_badged", nullable = false) // 알림을 읽었는지 안읽었는지
    private Boolean isBadged;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @Builder
    public Notification(String content, String reason, Boolean isBadged, User sender, User receiver) {
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.reason = reason;
        this.isBadged = isBadged;
        this.sender = sender;
        this.receiver = receiver;
    }

    public void readNotification() {
        this.isBadged = false;
    }

    public void activateBadge() {
        this.isBadged = true;
    }
}

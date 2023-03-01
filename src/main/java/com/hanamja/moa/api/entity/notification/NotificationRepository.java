package com.hanamja.moa.api.entity.notification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Modifying
    @Query(value = "UPDATE Notification n SET n.isBadged = false WHERE n.receiver.id = :receiverId AND n.isBadged = true")
    void updateNotificationState(@Param(value = "receiverId") Long receiverId);

    List<Notification> findAllByReceiver_Id(Long receiverId);
}

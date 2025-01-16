package com.linkmoa.source.domain.notification.repository;

import com.linkmoa.source.domain.notification.entity.Notification;
import com.linkmoa.source.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends JpaRepository<Notification,Long>,NotificationRepositoryCustom {

    @Modifying
    @Query("DELETE FROM Notification n WHERE n.senderEmail = :email OR n.receiverEmail = :email")
    void deleteAllBySenderEmailOrReceiverEmail(@Param("email") String email);
}

package com.linkmoa.source.domain.notify.repository;

import com.linkmoa.source.domain.notify.entity.Notify;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends JpaRepository<Notify,Long> {

    @Modifying
    @Query("DELETE FROM Notify n WHERE n.senderEmail = :email OR n.receiverEmail = :email")
    void deleteAllBySenderEmailOrReceiverEmail(@Param("email") String email);
}

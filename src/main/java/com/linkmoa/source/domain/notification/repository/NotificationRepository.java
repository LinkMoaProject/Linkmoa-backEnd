package com.linkmoa.source.domain.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.linkmoa.source.domain.member.entity.Member;
import com.linkmoa.source.domain.notification.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long>, NotificationRepositoryCustom {

	@Modifying
	@Query("DELETE FROM Notification n WHERE n.sender = :member OR n.receiver = :member")
	void deleteAllBySenderEmailOrReceiver(@Param("member") Member member);
}

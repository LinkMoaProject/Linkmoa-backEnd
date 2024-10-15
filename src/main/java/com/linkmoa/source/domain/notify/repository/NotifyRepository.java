package com.linkmoa.source.domain.notify.repository;

import com.linkmoa.source.domain.notify.entity.Notify;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotifyRepository extends JpaRepository<Notify,Long> {
}

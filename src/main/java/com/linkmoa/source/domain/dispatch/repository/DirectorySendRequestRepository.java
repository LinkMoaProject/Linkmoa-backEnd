package com.linkmoa.source.domain.dispatch.repository;

import com.linkmoa.source.domain.dispatch.entity.DirectoryTransmissionRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DirectorySendRequestRepository extends JpaRepository<DirectoryTransmissionRequest,Long> {
}

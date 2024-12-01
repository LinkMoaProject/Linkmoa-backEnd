package com.linkmoa.source.domain.directory.repository;

import com.linkmoa.source.domain.directory.entity.DirectoryTransmissionRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DirectorySendRequestRepository extends JpaRepository<DirectoryTransmissionRequest,Long> {
}

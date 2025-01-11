package com.linkmoa.source.domain.dispatch.repository;

import com.linkmoa.source.domain.dispatch.entity.DirectoryTransmissionRequest;
import com.linkmoa.source.domain.dispatch.repository.custom.DirectoryTransmissionRequestRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DirectoryTransmissionRequestRepository extends JpaRepository<DirectoryTransmissionRequest,Long>, DirectoryTransmissionRequestRepositoryCustom {
}

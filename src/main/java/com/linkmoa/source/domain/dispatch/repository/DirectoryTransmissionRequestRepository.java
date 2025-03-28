package com.linkmoa.source.domain.dispatch.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.linkmoa.source.domain.dispatch.constant.RequestStatus;
import com.linkmoa.source.domain.dispatch.entity.DirectoryTransmissionRequest;
import com.linkmoa.source.domain.dispatch.repository.custom.DirectoryTransmissionRequestRepositoryCustom;

public interface DirectoryTransmissionRequestRepository
	extends JpaRepository<DirectoryTransmissionRequest, Long>, DirectoryTransmissionRequestRepositoryCustom {

	Optional<DirectoryTransmissionRequest> findByDirectoryIdAndRequestStatus(Long directoryId,
		RequestStatus requestStatus);
}

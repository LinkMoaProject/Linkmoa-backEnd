package com.linkmoa.source.domain.dispatch.repository.custom;

import com.linkmoa.source.domain.dispatch.dto.response.DispatchDetailResponse;

import java.util.List;

public interface DirectoryTransmissionRequestRepositoryCustom {

	List<DispatchDetailResponse> findAllDirectoryTransmissionRequestByReceiverEmail(String receiverEmail);
}

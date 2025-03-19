package com.linkmoa.source.domain.dispatch.service.processor;

import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.directory.entity.Directory;
import com.linkmoa.source.domain.directory.service.DirectoryService;
import com.linkmoa.source.domain.dispatch.constant.RequestStatus;
import com.linkmoa.source.domain.dispatch.dto.request.DispatchProcessingRequest;
import com.linkmoa.source.domain.dispatch.dto.response.ApiDispatchResponseSpec;
import com.linkmoa.source.domain.dispatch.dto.response.DispatchDetailResponse;
import com.linkmoa.source.domain.dispatch.entity.DirectoryTransmissionRequest;
import com.linkmoa.source.domain.dispatch.error.DispatchErrorCode;
import com.linkmoa.source.domain.dispatch.exception.DispatchException;
import com.linkmoa.source.domain.dispatch.repository.DirectoryTransmissionRequestRepository;
import com.linkmoa.source.domain.member.entity.Member;
import com.linkmoa.source.domain.member.service.MemberService;
import com.linkmoa.source.domain.notification.constant.NotificationType;
import com.linkmoa.source.domain.page.entity.Page;
import com.linkmoa.source.domain.page.service.PageService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DirectoryTransmissionRequestProcessor implements DispatchProcessor {

	private final DirectoryTransmissionRequestRepository directoryTransmissionRequestRepository;
	private final MemberService memberService;
	private final PageService pageService;
	private final DirectoryService directoryService;

	@Override
	@Transactional
	public ApiDispatchResponseSpec<DispatchDetailResponse> processRequest(
		DispatchProcessingRequest dispatchProcessingRequest, PrincipalDetails principalDetails) {
		Long requestId = dispatchProcessingRequest.requestId();
		RequestStatus requestStatus = dispatchProcessingRequest.requestStatus();
		NotificationType notificationType = dispatchProcessingRequest.notificationType();

		//요청타입 검증
		validateNotificationType(NotificationType.TRANSMIT_DIRECTORY, notificationType);

		DirectoryTransmissionRequest directoryTransmissionRequest = directoryTransmissionRequestRepository.findById(
				requestId)
			.orElseThrow(() -> new DispatchException(DispatchErrorCode.TRANSMIT_DIRECTORY_REQUEST_NOT_FOUND));

		// 요청 상태 검증
		validateRequestStatus(directoryTransmissionRequest.getRequestStatus());

		Member member = memberService.findMemberByEmail(principalDetails.getEmail());

		validateReceiver(directoryTransmissionRequest.getReceiver().getEmail(), member.getEmail());
		directoryTransmissionRequest.changeDirectoryTransmissionRequestStatus(requestStatus);

		String successMessage;

		if (requestStatus == RequestStatus.ACCEPTED) {
			DirectoryTransmissionProcess(directoryTransmissionRequest);
			successMessage = "디렉토리 전송 요청을 수락하고 디렉토리를 이동했습니다.";
		} else {
			successMessage = "디렉토리 전송 요청을 거절했습니다.";
		}

		DispatchDetailResponse response = DispatchDetailResponse.builder()
			.id(directoryTransmissionRequest.getId())
			.requestStatus(directoryTransmissionRequest.getRequestStatus())
			.senderEmail(directoryTransmissionRequest.getSender().getEmail())
			.notificationType(NotificationType.TRANSMIT_DIRECTORY)
			.build();

		return ApiDispatchResponseSpec.<DispatchDetailResponse>builder()
			.httpStatusCode(HttpStatus.OK)
			.successMessage(successMessage)
			.data(response)
			.build();

	}

	public void DirectoryTransmissionProcess(DirectoryTransmissionRequest directoryTransmissionRequest) {
		Member receiver = memberService.findMemberByEmail(directoryTransmissionRequest.getReceiver().getEmail());
		Page receiverPersonalPage = pageService.getPersonalPage(receiver.getId());

		Long receiverPersonalRootDirectoryId = receiverPersonalPage.getRootDirectory().getId();
		Long transmissionDirectoryId = directoryTransmissionRequest.getDirectory().getId();

		Directory directory = directoryService.cloneDirectory(receiverPersonalRootDirectoryId, transmissionDirectoryId);

	}

}

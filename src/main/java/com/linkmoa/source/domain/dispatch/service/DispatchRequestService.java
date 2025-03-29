package com.linkmoa.source.domain.dispatch.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.directory.entity.Directory;
import com.linkmoa.source.domain.directory.error.DirectoryErrorCode;
import com.linkmoa.source.domain.directory.exception.DirectoryException;
import com.linkmoa.source.domain.directory.repository.DirectoryRepository;
import com.linkmoa.source.domain.dispatch.constant.RequestStatus;
import com.linkmoa.source.domain.dispatch.dto.request.DirectoryTransmissionRequestCreate;
import com.linkmoa.source.domain.dispatch.dto.request.SharePageInvitationRequestCreate;
import com.linkmoa.source.domain.dispatch.dto.response.DirectoryTransmissionResponse;
import com.linkmoa.source.domain.dispatch.dto.response.DispatchDetailResponse;
import com.linkmoa.source.domain.dispatch.dto.response.NotificationsDetailsResponse;
import com.linkmoa.source.domain.dispatch.dto.response.SharePageInvitationResponse;
import com.linkmoa.source.domain.dispatch.entity.DirectoryTransmissionRequest;
import com.linkmoa.source.domain.dispatch.entity.SharePageInvitationRequest;
import com.linkmoa.source.domain.dispatch.error.DispatchErrorCode;
import com.linkmoa.source.domain.dispatch.exception.DispatchException;
import com.linkmoa.source.domain.dispatch.repository.DirectoryTransmissionRequestRepository;
import com.linkmoa.source.domain.dispatch.repository.SharePageInvitationRequestRepository;
import com.linkmoa.source.domain.member.error.MemberErrorCode;
import com.linkmoa.source.domain.member.exception.MemberException;
import com.linkmoa.source.domain.member.service.MemberService;
import com.linkmoa.source.domain.notification.aop.annotation.NotificationApplied;
import com.linkmoa.source.domain.notification.repository.NotificationRepository;
import com.linkmoa.source.domain.notification.service.NotificationService;
import com.linkmoa.source.domain.page.contant.PageType;
import com.linkmoa.source.domain.page.entity.Page;
import com.linkmoa.source.domain.page.error.PageErrorCode;
import com.linkmoa.source.domain.page.exception.PageException;
import com.linkmoa.source.domain.page.repository.PageRepository;
import com.linkmoa.source.global.aop.annotation.ValidationApplied;
import com.linkmoa.source.global.spec.ApiResponseSpec;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DispatchRequestService {

	private final MemberService memberService;
	private final DirectoryRepository directoryRepository;
	private final DirectoryTransmissionRequestRepository directoryTransmissionRequestRepository;
	private final PageRepository pageRepository;
	private final SharePageInvitationRequestRepository sharePageInvitationRequestRepository;
	private final NotificationRepository notificationRepository;
	private final NotificationService notificationService;

	@Transactional
	@ValidationApplied
	@NotificationApplied
	public DirectoryTransmissionRequest createDirectoryTransmissionRequest(
		DirectoryTransmissionRequestCreate directoryTransmissionSendRequest,
		PrincipalDetails principalDetails) {

		if (!memberService.isMemberExist(directoryTransmissionSendRequest.receiverEmail())) {
			throw new MemberException(MemberErrorCode.MEMBER_NOT_FOUND_EMAIL);
		}

		DirectoryTransmissionRequest existingRequest = directoryTransmissionRequestRepository
			.findByDirectoryIdAndRequestStatus(directoryTransmissionSendRequest.directoryId(), RequestStatus.WAITING)
			.orElse(null);

		if (existingRequest != null) {
			throw new DispatchException(DispatchErrorCode.TRANSMIT_DIRECTORY_REQUEST_ALREADY_EXIST);
		}

		DirectoryTransmissionRequest acceptedRequest = directoryTransmissionRequestRepository
			.findByDirectoryIdAndRequestStatus(directoryTransmissionSendRequest.directoryId(), RequestStatus.ACCEPTED)
			.orElse(null);

		if (acceptedRequest != null) {
			throw new DispatchException(DispatchErrorCode.TRANSMIT_DIRECTORY_REQUEST_ACCEPTED_EXIST);
		}

		Directory directory = directoryRepository.findById(directoryTransmissionSendRequest.directoryId())
			.orElseThrow(() -> new DirectoryException(DirectoryErrorCode.DIRECTORY_NOT_FOUND));

		DirectoryTransmissionRequest directoryTransmissionRequest = DirectoryTransmissionRequest.builder()
			.sender(principalDetails.getMember())
			.receiver(memberService.findMemberByEmail(directoryTransmissionSendRequest.receiverEmail()))
			.directory(directory)
			.build();

		return directoryTransmissionRequestRepository.save(directoryTransmissionRequest);
	}

	public ApiResponseSpec<DirectoryTransmissionResponse> mapToDirectorySendResponse(
		DirectoryTransmissionRequest directoryTransmissionRequest) {
		DirectoryTransmissionResponse directoryTransmissionResponse = DirectoryTransmissionResponse.builder()
			.directoryName(directoryTransmissionRequest.getDirectory().getDirectoryName())
			.receiverEmail(directoryTransmissionRequest.getReceiver().getEmail())
			.senderEmail(directoryTransmissionRequest.getSender().getEmail())
			.directoryTransmissionId(directoryTransmissionRequest.getRequestId())
			.build();

		return ApiResponseSpec.success(
			HttpStatus.OK,
			"디렉토리 전송 요청을 보냈습니다.",
			directoryTransmissionResponse
		);
	}

	@Transactional
	@ValidationApplied
	@NotificationApplied
	public SharePageInvitationRequest createSharePageInviteRequest(
		SharePageInvitationRequestCreate sharePageInvitationRequestCreate, PrincipalDetails principalDetails) {

		if (!memberService.isMemberExist(sharePageInvitationRequestCreate.receiverEmail())) {
			throw new MemberException(MemberErrorCode.MEMBER_NOT_FOUND_EMAIL); // 유저가 없으면 예외 발생
		}

		Page page = pageRepository.findById(sharePageInvitationRequestCreate.baseRequest().pageId())
			.orElseThrow(() -> new PageException(PageErrorCode.PAGE_NOT_FOUND));

		if (page.getPageType() == PageType.PERSONAL) {
			throw new PageException(PageErrorCode.CANNOT_INVITE_TO_PERSONAL_PAGE);
		}

		SharePageInvitationRequest existingRequest = sharePageInvitationRequestRepository.
			findByPageIdAndRequestStatus(page.getId(), RequestStatus.WAITING)
			.orElse(null);

		if (existingRequest != null) {
			throw new DispatchException(DispatchErrorCode.SHARE_PAGE_INVITATION_REQUEST_ALREADY_EXIST);
		}

		SharePageInvitationRequest acceptedRequest = sharePageInvitationRequestRepository
			.findByPageIdAndRequestStatus(page.getId(), RequestStatus.ACCEPTED)
			.orElse(null);

		if (acceptedRequest != null) {
			throw new DispatchException(DispatchErrorCode.SHARE_PAGE_INVITATION_REQUEST_ACCEPTED_EXIST);
		}

		SharePageInvitationRequest sharePageInvitationRequest = SharePageInvitationRequest.builder()
			.sender(principalDetails.getMember())
			.receiver(memberService.findMemberByEmail(sharePageInvitationRequestCreate.receiverEmail()))
			.page(page)
			.permissionType(sharePageInvitationRequestCreate.permissionType())
			.build();

		return sharePageInvitationRequestRepository.save(sharePageInvitationRequest);
	}

	public ApiResponseSpec<SharePageInvitationResponse> mapToPageInviteRequestResponse(
		com.linkmoa.source.domain.dispatch.entity.SharePageInvitationRequest sharePageInvitationRequest) {

		SharePageInvitationResponse sharePageInvitationResponse = SharePageInvitationResponse.builder()
			.pageTitle(sharePageInvitationRequest.getPage().getPageTitle())
			.receiverEmail(sharePageInvitationRequest.getReceiver().getEmail())
			.senderEmail(sharePageInvitationRequest.getSender().getEmail())
			.pageInvitationRequestId(sharePageInvitationRequest.getId())
			.build();

		return ApiResponseSpec.success(
			HttpStatus.OK,
			"공유 페이지 초대를 보냈습니다.",
			sharePageInvitationResponse
		);
	}

	public List<DispatchDetailResponse> findSharePageInvitationsForReceiver(String receiverEmail) {
		List<DispatchDetailResponse> allSharePageInvitationsByReceiverEmail =
			sharePageInvitationRequestRepository.findAllSharePageInvitationsByReceiverEmail(receiverEmail);

		return allSharePageInvitationsByReceiverEmail;
	}

	public List<DispatchDetailResponse> findDirectoryDirectoryTransmissionsForReceiver(String receiverEmail) {
		List<DispatchDetailResponse> allDirectoryTransmissionRequestByReceiverEmail =
			directoryTransmissionRequestRepository.findAllDirectoryTransmissionRequestByReceiverEmail(receiverEmail);

		return allDirectoryTransmissionRequestByReceiverEmail;
	}

	@Transactional
	public ApiResponseSpec<NotificationsDetailsResponse> findAllNotificationsForReceiver(String receiverEmail) {
		NotificationsDetailsResponse notificationDetails = NotificationsDetailsResponse.builder()
			.DirectoryTransmissionRequests(findDirectoryDirectoryTransmissionsForReceiver(receiverEmail))
			.SharePageInvitationRequests(findSharePageInvitationsForReceiver(receiverEmail))
			.build();

		notificationRepository.updateUnreadNotificationsToReadByReceiverEmail(receiverEmail);

		notificationService.sendUnreadNotificationCount(receiverEmail);

		return ApiResponseSpec.success(
			HttpStatus.OK,
			"알람 목록을 조회했습니다.",
			notificationDetails
		);

	}

}

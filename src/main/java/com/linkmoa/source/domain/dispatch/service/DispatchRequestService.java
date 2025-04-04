package com.linkmoa.source.domain.dispatch.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.directory.entity.Directory;
import com.linkmoa.source.domain.directory.error.DirectoryErrorCode;
import com.linkmoa.source.domain.directory.exception.DirectoryException;
import com.linkmoa.source.domain.directory.repository.DirectoryRepository;
import com.linkmoa.source.domain.dispatch.constant.RequestStatus;
import com.linkmoa.source.domain.dispatch.dto.request.DirectoryTransmissionDto;
import com.linkmoa.source.domain.dispatch.dto.request.SharePageInvitationRequestDto;
import com.linkmoa.source.domain.dispatch.dto.response.DispatchDetailResponse;
import com.linkmoa.source.domain.dispatch.dto.response.NotificationsDetailsResponse;
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
		DirectoryTransmissionDto.Request request,
		PrincipalDetails principalDetails) {

		if (!memberService.isMemberExist(request.receiverEmail())) {
			throw new MemberException(MemberErrorCode.MEMBER_NOT_FOUND_EMAIL);
		}

		DirectoryTransmissionRequest existingRequest = directoryTransmissionRequestRepository
			.findByDirectoryIdAndRequestStatus(request.directoryId(), RequestStatus.WAITING)
			.orElse(null);

		if (existingRequest != null) {
			throw new DispatchException(DispatchErrorCode.TRANSMIT_DIRECTORY_REQUEST_ALREADY_EXIST);
		}

		DirectoryTransmissionRequest acceptedRequest = directoryTransmissionRequestRepository
			.findByDirectoryIdAndRequestStatus(request.directoryId(), RequestStatus.ACCEPTED)
			.orElse(null);

		if (acceptedRequest != null) {
			throw new DispatchException(DispatchErrorCode.TRANSMIT_DIRECTORY_REQUEST_ACCEPTED_EXIST);
		}

		Directory directory = directoryRepository.findById(request.directoryId())
			.orElseThrow(() -> new DirectoryException(DirectoryErrorCode.DIRECTORY_NOT_FOUND));

		DirectoryTransmissionRequest directoryTransmissionRequest = DirectoryTransmissionRequest.builder()
			.sender(principalDetails.getMember())
			.receiver(memberService.findMemberByEmail(request.receiverEmail()))
			.directory(directory)
			.build();

		return directoryTransmissionRequestRepository.save(directoryTransmissionRequest);
	}

	public DirectoryTransmissionDto.Response mapToDirectorySendResponse(
		DirectoryTransmissionRequest request) {

		return DirectoryTransmissionDto.Response.builder()
			.directoryName(request.getDirectory().getDirectoryName())
			.receiverEmail(request.getReceiver().getEmail())
			.senderEmail(request.getSender().getEmail())
			.directoryTransmissionId(request.getRequestId())
			.build();
	}

	@Transactional
	@ValidationApplied
	@NotificationApplied
	public SharePageInvitationRequest createSharePageInviteRequest(
		SharePageInvitationRequestDto.Request request, PrincipalDetails principalDetails) {

		if (!memberService.isMemberExist(request.receiverEmail())) {
			throw new MemberException(MemberErrorCode.MEMBER_NOT_FOUND_EMAIL); // 유저가 없으면 예외 발생
		}

		Page page = pageRepository.findById(request.baseRequest().pageId())
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
			.receiver(memberService.findMemberByEmail(request.receiverEmail()))
			.page(page)
			.permissionType(request.permissionType())
			.build();

		return sharePageInvitationRequestRepository.save(sharePageInvitationRequest);
	}

	public SharePageInvitationRequestDto.Response mapToPageInviteRequestResponse(
		SharePageInvitationRequest request) {

		return SharePageInvitationRequestDto.Response.builder()
			.pageTitle(request.getPage().getPageTitle())
			.receiverEmail(request.getReceiver().getEmail())
			.senderEmail(request.getSender().getEmail())
			.pageInvitationRequestId(request.getId())
			.build();
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
	public NotificationsDetailsResponse findAllNotificationsForReceiver(String receiverEmail) {
		NotificationsDetailsResponse notificationDetails = NotificationsDetailsResponse.builder()
			.DirectoryTransmissionRequests(findDirectoryDirectoryTransmissionsForReceiver(receiverEmail))
			.SharePageInvitationRequests(findSharePageInvitationsForReceiver(receiverEmail))
			.build();

		notificationRepository.updateUnreadNotificationsToReadByReceiverEmail(receiverEmail);

		notificationService.sendUnreadNotificationCount(receiverEmail);

		return notificationDetails;

	}

}

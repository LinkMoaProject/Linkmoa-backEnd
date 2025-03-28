package com.linkmoa.source.domain.dispatch.service.processor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.dispatch.constant.RequestStatus;
import com.linkmoa.source.domain.dispatch.dto.request.DispatchProcessingRequest;
import com.linkmoa.source.domain.dispatch.dto.response.ApiDispatchResponseSpec;
import com.linkmoa.source.domain.dispatch.dto.response.DispatchDetailResponse;
import com.linkmoa.source.domain.dispatch.entity.SharePageInvitationRequest;
import com.linkmoa.source.domain.dispatch.error.DispatchErrorCode;
import com.linkmoa.source.domain.dispatch.exception.DispatchException;
import com.linkmoa.source.domain.dispatch.repository.SharePageInvitationRequestRepository;
import com.linkmoa.source.domain.member.entity.Member;
import com.linkmoa.source.domain.member.service.MemberService;
import com.linkmoa.source.domain.memberPageLink.constant.PermissionType;
import com.linkmoa.source.domain.memberPageLink.entity.MemberPageLink;
import com.linkmoa.source.domain.memberPageLink.repository.MemberPageLinkRepository;
import com.linkmoa.source.domain.notification.constant.NotificationType;
import com.linkmoa.source.domain.page.entity.Page;
import com.linkmoa.source.domain.page.repository.PageRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SharePageInvitationRequestProcessor implements DispatchProcessor {

	private final PageRepository pageRepository;
	private final SharePageInvitationRequestRepository sharePageInvitationRequestRepository;
	private final MemberPageLinkRepository memberPageLinkRepository;
	private final MemberService memberService;

	@Override
	@Transactional
	public ApiDispatchResponseSpec<DispatchDetailResponse> processRequest(
		DispatchProcessingRequest dispatchProcessingRequest, PrincipalDetails principalDetails) {

		Long requestId = dispatchProcessingRequest.requestId();
		RequestStatus requestStatus = dispatchProcessingRequest.requestStatus();
		NotificationType notificationType = dispatchProcessingRequest.notificationType();

		// 요청 타입 검증
		validateNotificationType(NotificationType.INVITE_PAGE, notificationType);

		SharePageInvitationRequest sharePageInvitationRequest = sharePageInvitationRequestRepository.findById(
				requestId).
			orElseThrow(() -> new DispatchException(DispatchErrorCode.SHARE_PAGE_INVITATION_REQUEST_NOT_FOUND));

		// 요청 상태 검증
		validateRequestStatus(sharePageInvitationRequest.getRequestStatus());

		Page page = sharePageInvitationRequest.getPage();
		Member member = memberService.findMemberByEmail(principalDetails.getEmail());
		PermissionType permissionType = sharePageInvitationRequest.getPermissionType();

		// 수신자 및 현재 유저 검증
		validateReceiver(sharePageInvitationRequest.getReceiver().getEmail(), member.getEmail());
		sharePageInvitationRequest.changeRequestStatus(requestStatus);

		if (requestStatus == RequestStatus.ACCEPTED) {
			acceptSharePageInvitation(page, member, permissionType);
		}

		String successMessage = requestStatus == RequestStatus.ACCEPTED
			? "공유 페이지 초대를 수락했습니다."
			: "공유 페이지 초대를 거절했습니다.";

		DispatchDetailResponse response = DispatchDetailResponse.builder()
			.id(sharePageInvitationRequest.getId())
			.requestStatus(sharePageInvitationRequest.getRequestStatus())
			.senderEmail(sharePageInvitationRequest.getSender().getEmail())
			.notificationType(NotificationType.INVITE_PAGE)
			.build();

		return ApiDispatchResponseSpec.<DispatchDetailResponse>builder()
			.httpStatusCode(HttpStatus.OK)
			.successMessage(successMessage)
			.data(response)
			.build();

	}

	public void acceptSharePageInvitation(Page page, Member member, PermissionType permissionType) {
		boolean existingLink = memberPageLinkRepository.existsByMemberAndPage(member.getId(), page.getId());

		if (!existingLink) {
			MemberPageLink memberPageLink = MemberPageLink.builder()
				.page(page)
				.member(member)
				.permissionType(permissionType)
				.build();
			memberPageLinkRepository.save(memberPageLink);
		}
	}

}

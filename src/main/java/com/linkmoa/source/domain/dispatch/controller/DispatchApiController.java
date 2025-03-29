package com.linkmoa.source.domain.dispatch.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.directory.dto.response.ApiDirectoryResponseSpec;
import com.linkmoa.source.domain.dispatch.dto.request.DirectoryTransmissionRequestCreate;
import com.linkmoa.source.domain.dispatch.dto.request.DispatchProcessingRequest;
import com.linkmoa.source.domain.dispatch.dto.request.SharePageInvitationRequestCreate;
import com.linkmoa.source.domain.dispatch.dto.response.ApiDispatchResponseSpec;
import com.linkmoa.source.domain.dispatch.dto.response.DirectoryTransmissionResponse;
import com.linkmoa.source.domain.dispatch.dto.response.DispatchDetailResponse;
import com.linkmoa.source.domain.dispatch.dto.response.NotificationsDetailsResponse;
import com.linkmoa.source.domain.dispatch.dto.response.SharePageInvitationResponse;
import com.linkmoa.source.domain.dispatch.service.DispatchRequestService;
import com.linkmoa.source.domain.dispatch.service.processor.DirectoryTransmissionRequestProcessor;
import com.linkmoa.source.domain.dispatch.service.processor.SharePageInvitationRequestProcessor;
import com.linkmoa.source.domain.page.dto.response.ApiPageResponseSpec;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dispatch")
public class DispatchApiController {

	private final DispatchRequestService dispatchRequestService;
	private final DirectoryTransmissionRequestProcessor directoryTransmissionRequestProcessor;
	private final SharePageInvitationRequestProcessor sharePageInvitationRequestProcessor;

	@PostMapping("/directory-transmissions")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiDirectoryResponseSpec<DirectoryTransmissionResponse>> transmitDirectory(
		@RequestBody @Validated DirectoryTransmissionRequestCreate directoryTransmissionRequestCreate,
		@AuthenticationPrincipal PrincipalDetails principalDetails) {
		ApiDirectoryResponseSpec<DirectoryTransmissionResponse> directoryTransmissionResponse = dispatchRequestService.mapToDirectorySendResponse(
			dispatchRequestService.createDirectoryTransmissionRequest(
				directoryTransmissionRequestCreate,
				principalDetails)
		);
		return ResponseEntity.ok().body(directoryTransmissionResponse);
	}

	@PatchMapping("/directory-transmissions/status")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiDispatchResponseSpec<DispatchDetailResponse>> processDirectoryTransmission(
		@RequestBody @Validated DispatchProcessingRequest dispatchProcessingRequest,
		@AuthenticationPrincipal PrincipalDetails principalDetails) {

		ApiDispatchResponseSpec<DispatchDetailResponse> directoryTransmissionResponse = directoryTransmissionRequestProcessor.processRequest(
			dispatchProcessingRequest, principalDetails);

		return ResponseEntity.ok().body(directoryTransmissionResponse);
	}

	@PostMapping("/share-page-invitations")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiPageResponseSpec<SharePageInvitationResponse>> inviteSharePage(
		@RequestBody @Validated SharePageInvitationRequestCreate pageInvitationRequest,
		@AuthenticationPrincipal PrincipalDetails principalDetails) {
		ApiPageResponseSpec<SharePageInvitationResponse> pageInviteRequestResponse =
			dispatchRequestService.mapToPageInviteRequestResponse(
				dispatchRequestService.createSharePageInviteRequest(pageInvitationRequest, principalDetails));

		return ResponseEntity.ok().body(pageInviteRequestResponse);
	}

	@PatchMapping("/share-page-invitations/status")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiDispatchResponseSpec<DispatchDetailResponse>> processSharePageInvitation(
		@RequestBody @Validated DispatchProcessingRequest dispatchProcessingRequest,
		@AuthenticationPrincipal PrincipalDetails principalDetails) {

		ApiDispatchResponseSpec<DispatchDetailResponse> sharePageInvitationResponse = sharePageInvitationRequestProcessor.processRequest(
			dispatchProcessingRequest, principalDetails);

		return ResponseEntity.ok().body(sharePageInvitationResponse);
	}

	@GetMapping("/notifications")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiDispatchResponseSpec<NotificationsDetailsResponse>> getAllNotification(
		@AuthenticationPrincipal PrincipalDetails principalDetails) {

		ApiDispatchResponseSpec<NotificationsDetailsResponse> allNotificationsForReceiver =
			dispatchRequestService.findAllNotificationsForReceiver(principalDetails.getEmail());

		return ResponseEntity.ok().body(allNotificationsForReceiver);
	}

}

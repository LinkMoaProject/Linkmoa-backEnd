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
import com.linkmoa.source.domain.dispatch.dto.request.DirectoryTransmissionRequestCreate;
import com.linkmoa.source.domain.dispatch.dto.request.DispatchProcessingRequest;
import com.linkmoa.source.domain.dispatch.dto.request.SharePageInvitationRequestCreate;
import com.linkmoa.source.domain.dispatch.dto.response.DirectoryTransmissionResponse;
import com.linkmoa.source.domain.dispatch.dto.response.DispatchDetailResponse;
import com.linkmoa.source.domain.dispatch.dto.response.NotificationsDetailsResponse;
import com.linkmoa.source.domain.dispatch.dto.response.SharePageInvitationResponse;
import com.linkmoa.source.domain.dispatch.service.DispatchRequestService;
import com.linkmoa.source.domain.dispatch.service.processor.DirectoryTransmissionRequestProcessor;
import com.linkmoa.source.domain.dispatch.service.processor.SharePageInvitationRequestProcessor;
import com.linkmoa.source.global.spec.ApiResponseSpec;

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
	public ResponseEntity<ApiResponseSpec<DirectoryTransmissionResponse>> transmitDirectory(
		@RequestBody @Validated DirectoryTransmissionRequestCreate directoryTransmissionRequestCreate,
		@AuthenticationPrincipal PrincipalDetails principalDetails) {
		ApiResponseSpec<DirectoryTransmissionResponse> directoryTransmissionResponse = dispatchRequestService.mapToDirectorySendResponse(
			dispatchRequestService.createDirectoryTransmissionRequest(
				directoryTransmissionRequestCreate,
				principalDetails)
		);
		return ResponseEntity.ok().body(directoryTransmissionResponse);
	}

	@PatchMapping("/directory-transmissions/status")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiResponseSpec<DispatchDetailResponse>> processDirectoryTransmission(
		@RequestBody @Validated DispatchProcessingRequest dispatchProcessingRequest,
		@AuthenticationPrincipal PrincipalDetails principalDetails) {

		ApiResponseSpec<DispatchDetailResponse> response = directoryTransmissionRequestProcessor.processRequest(
			dispatchProcessingRequest, principalDetails);

		return ResponseEntity.ok().body(response);
	}

	@PostMapping("/share-page-invitations")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiResponseSpec<SharePageInvitationResponse>> inviteSharePage(
		@RequestBody @Validated SharePageInvitationRequestCreate pageInvitationRequest,
		@AuthenticationPrincipal PrincipalDetails principalDetails) {
		ApiResponseSpec<SharePageInvitationResponse> response =
			dispatchRequestService.mapToPageInviteRequestResponse(
				dispatchRequestService.createSharePageInviteRequest(pageInvitationRequest, principalDetails));

		return ResponseEntity.ok().body(response);
	}

	@PatchMapping("/share-page-invitations/status")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiResponseSpec<DispatchDetailResponse>> processSharePageInvitation(
		@RequestBody @Validated DispatchProcessingRequest dispatchProcessingRequest,
		@AuthenticationPrincipal PrincipalDetails principalDetails) {

		ApiResponseSpec<DispatchDetailResponse> response = sharePageInvitationRequestProcessor.processRequest(
			dispatchProcessingRequest, principalDetails);

		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/notifications")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiResponseSpec<NotificationsDetailsResponse>> getAllNotification(
		@AuthenticationPrincipal PrincipalDetails principalDetails) {

		ApiResponseSpec<NotificationsDetailsResponse> response =
			dispatchRequestService.findAllNotificationsForReceiver(principalDetails.getEmail());

		return ResponseEntity.ok().body(response);
	}

}

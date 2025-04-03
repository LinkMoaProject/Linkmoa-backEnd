package com.linkmoa.source.domain.dispatch.controller;

import org.springframework.http.HttpStatus;
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
import com.linkmoa.source.domain.dispatch.dto.request.DirectoryTransmissionDto;
import com.linkmoa.source.domain.dispatch.dto.request.DispatchProcessingRequest;
import com.linkmoa.source.domain.dispatch.dto.request.SharePageInvitationRequestDto;
import com.linkmoa.source.domain.dispatch.dto.response.DispatchDetailResponse;
import com.linkmoa.source.domain.dispatch.dto.response.NotificationsDetailsResponse;
import com.linkmoa.source.domain.dispatch.service.DispatchMessageResolver;
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
	public ResponseEntity<ApiResponseSpec<DirectoryTransmissionDto.Response>> transmitDirectory(
		@RequestBody @Validated DirectoryTransmissionDto.Requeest directoryTransmissionRequestCreate,
		@AuthenticationPrincipal PrincipalDetails principalDetails) {
		return ResponseEntity.ok().body(ApiResponseSpec.success(
			HttpStatus.OK,
			"디렉토리 전송 요청을 보냈습니다.",
			dispatchRequestService.mapToDirectorySendResponse(
				dispatchRequestService.createDirectoryTransmissionRequest(
					directoryTransmissionRequestCreate,
					principalDetails)
			)));
	}

	@PatchMapping("/directory-transmissions/status")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiResponseSpec<DispatchDetailResponse>> processDirectoryTransmission(
		@RequestBody @Validated DispatchProcessingRequest dispatchProcessingRequest,
		@AuthenticationPrincipal PrincipalDetails principalDetails) {
		DispatchDetailResponse response = directoryTransmissionRequestProcessor.processRequest(
			dispatchProcessingRequest, principalDetails);
		return ResponseEntity.ok().body(ApiResponseSpec.success(
			HttpStatus.OK,
			DispatchMessageResolver.resolve(response.requestStatus(), response.notificationType()),
			response
		));
	}

	@PostMapping("/share-page-invitations")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiResponseSpec<SharePageInvitationRequestDto.Response>> inviteSharePage(
		@RequestBody @Validated SharePageInvitationRequestDto.Request pageInvitationRequest,
		@AuthenticationPrincipal PrincipalDetails principalDetails) {

		return ResponseEntity.ok().body(ApiResponseSpec.success(
			HttpStatus.OK,
			"공유 페이지 초대를 보냈습니다.",
			dispatchRequestService.mapToPageInviteRequestResponse(
				dispatchRequestService.createSharePageInviteRequest(pageInvitationRequest, principalDetails))));
	}

	@PatchMapping("/share-page-invitations/status")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiResponseSpec<DispatchDetailResponse>> processSharePageInvitation(
		@RequestBody @Validated DispatchProcessingRequest dispatchProcessingRequest,
		@AuthenticationPrincipal PrincipalDetails principalDetails) {

		DispatchDetailResponse response = sharePageInvitationRequestProcessor.processRequest(
			dispatchProcessingRequest, principalDetails);

		return ResponseEntity.ok().body(ApiResponseSpec.success(
			HttpStatus.OK,
			DispatchMessageResolver.resolve(response.requestStatus(), response.notificationType()),
			response
		));
	}

	@GetMapping("/notifications")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiResponseSpec<NotificationsDetailsResponse>> getAllNotification(
		@AuthenticationPrincipal PrincipalDetails principalDetails) {

		return ResponseEntity.ok().body(ApiResponseSpec.success(
			HttpStatus.OK,
			"알람 목록을 조회했습니다.",
			dispatchRequestService.findAllNotificationsForReceiver(principalDetails.getEmail())
		));
	}

}

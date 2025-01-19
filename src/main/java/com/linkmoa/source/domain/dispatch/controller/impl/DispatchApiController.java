package com.linkmoa.source.domain.dispatch.controller.impl;

import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.directory.dto.response.ApiDirectoryResponseSpec;
import com.linkmoa.source.domain.dispatch.dto.request.DispatchProcessingRequest;
import com.linkmoa.source.domain.dispatch.dto.request.SharePageInvitationRequestCreate;
import com.linkmoa.source.domain.dispatch.dto.response.*;
import com.linkmoa.source.domain.dispatch.controller.spec.DispatchApiSpecification;
import com.linkmoa.source.domain.dispatch.dto.request.DirectoryTransmissionRequestCreate;
import com.linkmoa.source.domain.dispatch.service.DispatchRequestService;
import com.linkmoa.source.domain.dispatch.service.processor.SharePageInvitationRequestProcessor;
import com.linkmoa.source.domain.page.dto.response.ApiPageResponseSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dispatch")
public class DispatchApiController implements DispatchApiSpecification {

    //[BE] [공유 페이지 초대 수락 또는 거절] 공유 페이지 초대 수락 또는 거절 구현

    private final DispatchRequestService dispatchRequestService;
    private final SharePageInvitationRequestProcessor sharePageInvitationRequestProcessor;

    public ResponseEntity<ApiDirectoryResponseSpec<DirectoryTransmissionResponse>> transmitDirectory(
            DirectoryTransmissionRequestCreate directoryTransmissionRequestCreate,
            PrincipalDetails principalDetails) {
        ApiDirectoryResponseSpec<DirectoryTransmissionResponse> directoryTransmissionResponse = dispatchRequestService.mapToDirectorySendResponse(
                dispatchRequestService.createDirectoryTransmissionRequest(
                        directoryTransmissionRequestCreate,
                        principalDetails)
        );
        return ResponseEntity.ok().body(directoryTransmissionResponse);
    }


    public ResponseEntity<ApiPageResponseSpec<SharePageInvitationResponse>> inviteSharePage(
            SharePageInvitationRequestCreate pageInvitationRequest,
            PrincipalDetails principalDetails) {
        ApiPageResponseSpec<SharePageInvitationResponse> pageInviteRequestResponse =
                dispatchRequestService.mapToPageInviteRequestResponse(dispatchRequestService.createSharePageInviteRequest(pageInvitationRequest, principalDetails));

        return ResponseEntity.ok().body(pageInviteRequestResponse);
    }

    public ResponseEntity<ApiDispatchResponseSpec<DispatchDetailResponse>> processSharePageInvitation(
            DispatchProcessingRequest dispatchProcessingRequest,
            PrincipalDetails principalDetails) {

        ApiDispatchResponseSpec<DispatchDetailResponse> sharePageInvitationResponse = sharePageInvitationRequestProcessor.processRequest(
                dispatchProcessingRequest, principalDetails);

        return ResponseEntity.ok().body(sharePageInvitationResponse);
    }

    public ResponseEntity<ApiDispatchResponseSpec<NotificationsDetailsResponse>> getAllNotification(
            PrincipalDetails principalDetails) {

        ApiDispatchResponseSpec<NotificationsDetailsResponse> allNotificationsForReceiver =
                dispatchRequestService.findAllNotificationsForReceiver(principalDetails.getEmail());

        return ResponseEntity.ok().body(allNotificationsForReceiver);
    }


}

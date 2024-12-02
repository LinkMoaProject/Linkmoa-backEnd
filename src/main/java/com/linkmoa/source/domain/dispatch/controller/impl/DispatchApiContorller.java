package com.linkmoa.source.domain.dispatch.controller.impl;

import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.directory.dto.response.ApiDirectoryResponseSpec;
import com.linkmoa.source.domain.dispatch.dto.request.SharePageInvitationRequestCreate;
import com.linkmoa.source.domain.dispatch.dto.response.DirectorySendResponse;
import com.linkmoa.source.domain.dispatch.controller.spec.DispatchApiSpecification;
import com.linkmoa.source.domain.dispatch.dto.request.DirectoryTransmissionSendRequest;
import com.linkmoa.source.domain.dispatch.dto.response.SharePageInvitationRequestCreateResponse;
import com.linkmoa.source.domain.dispatch.service.DispatchService;
import com.linkmoa.source.domain.page.dto.response.ApiPageResponseSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dispatch")
public class DispatchApiContorller implements DispatchApiSpecification {

    private final DispatchService dispatchService;

    public ResponseEntity<ApiDirectoryResponseSpec<DirectorySendResponse>> sendDirectory(
            DirectoryTransmissionSendRequest directoryTransmissionSendRequest,
            PrincipalDetails principalDetails) {
        ApiDirectoryResponseSpec<DirectorySendResponse> direcotrySendResponse = dispatchService.mapToDirectorySendResponse(
                dispatchService.createDirectoryTransmissionRequest(
                        directoryTransmissionSendRequest,
                        principalDetails)
        );
        return ResponseEntity.ok().body(direcotrySendResponse);
    }

    public ResponseEntity<ApiPageResponseSpec<SharePageInvitationRequestCreateResponse>> inviteSharePage(
            SharePageInvitationRequestCreate pageInvitationRequest,
            PrincipalDetails principalDetails) {
        ApiPageResponseSpec<SharePageInvitationRequestCreateResponse> pageInviteRequestResponse =
                dispatchService.mapToPageInviteRequestResponse(dispatchService.createSharePageInviteRequest(pageInvitationRequest, principalDetails));

        return ResponseEntity.ok().body(pageInviteRequestResponse);
    }

}

package com.linkmoa.source.domain.dispatch.controller.spec;

import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.directory.dto.response.ApiDirectoryResponseSpec;
import com.linkmoa.source.domain.dispatch.dto.request.DispatchProcessingRequest;
import com.linkmoa.source.domain.dispatch.dto.request.SharePageInvitationRequest;
import com.linkmoa.source.domain.dispatch.dto.response.*;
import com.linkmoa.source.domain.directory.error.DirectoryErrorCode;
import com.linkmoa.source.domain.dispatch.dto.request.DirectoryTransmissionRequest;
import com.linkmoa.source.domain.dispatch.error.DispatchErrorCode;
import com.linkmoa.source.domain.page.dto.response.ApiPageResponseSpec;
import com.linkmoa.source.domain.page.error.PageErrorCode;
import com.linkmoa.source.global.swagger.ApiErrorCodeExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface DispatchApiSpecification {

    @Tag(name = "Dispatch", description = "디렉토리 전송 요청 API")
    @Operation(summary = "디렉토리 전송 요청", description = "다른 유저에게 디렉토리 전송 요청을 보냅니다.")
    @ApiErrorCodeExamples(DirectoryErrorCode.class)
    @PostMapping("/directory-transmissions")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiDirectoryResponseSpec<DirectoryTransmissionResponse>> sendDirectory(
            @RequestBody @Validated DirectoryTransmissionRequest directoryTransmissionRequest,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    );


    @Tag(name = "Dispatch", description = "공유 페이지 요청 API")
    @Operation(summary = "공유 페이지 사용자 초대", description = "공유 페이지에 사용자 초대 요청을 보냅니다.")
    @ApiErrorCodeExamples(PageErrorCode.class)
    @PostMapping("/share-page-invitations")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiPageResponseSpec<SharePageInvitationResponse>> inviteSharePage(
            @RequestBody @Validated SharePageInvitationRequest sharePageInvitationRequest,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    );

    @Tag(name="Dispatch",description = "공유 페이지 초대 요청 처리 API")
    @Operation(summary = "공유 페이지 초대 요청 처리( 수락 , 거절 )", description = "공유 페이지 초대 요청 수락 또는 거절을 수행합니다.")
    @ApiErrorCodeExamples(DispatchErrorCode.class)
    @PatchMapping("/share-page-invitations/status")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiDispatchResponseSpec<DispatchDetailResponse>> processSharePageInvitation(
            @RequestBody @Validated DispatchProcessingRequest dispatchProcessingRequest,
            @AuthenticationPrincipal PrincipalDetails principalDetails
            );

    @Tag(name="Dispatch",description = "알람 목록 조회 수신 API")
    @Operation(summary = "알람 목록 조회 수신 ",description = "수신된 알람 목록을 조회합니다.")
    @ApiErrorCodeExamples(DispatchErrorCode.class)
    @GetMapping("/notifications")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiDispatchResponseSpec<NotificationsDetailsResponse>> getAllNotification(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    );


}

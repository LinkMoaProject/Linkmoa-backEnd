package com.linkmoa.source.domain.dispatch.controller.spec;

import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.directory.dto.response.ApiDirectoryResponseSpec;
import com.linkmoa.source.domain.dispatch.dto.request.SharePageInvitationRequestCreate;
import com.linkmoa.source.domain.dispatch.dto.response.DirectorySendResponse;
import com.linkmoa.source.domain.directory.error.DirectoryErrorCode;
import com.linkmoa.source.domain.dispatch.dto.request.DirectoryTransmissionSendRequest;
import com.linkmoa.source.domain.dispatch.dto.response.SharePageInvitationRequestCreateResponse;
import com.linkmoa.source.domain.page.dto.response.ApiPageResponseSpec;
import com.linkmoa.source.domain.page.error.PageErrorCode;
import com.linkmoa.source.global.swagger.ApiErrorCodeExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface DispatchApiSpecification {

    @Tag(name = "Post", description = "요청 관련 API")
    @Operation(summary = "디렉토리 전송 요청", description = "다른 유저에게 디렉토리 전송 요청을 보냅니다.")
    @ApiErrorCodeExamples(DirectoryErrorCode.class)
    @PostMapping("/directory-transmissions")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiDirectoryResponseSpec<DirectorySendResponse>> sendDirectory(
            @RequestBody @Validated DirectoryTransmissionSendRequest directoryTransmissionSendRequest,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    );


    @Tag(name = "Post", description = "요청 관련 API")
    @Operation(summary = "공유 페이지 사용자 초대", description = "공유 페이지에 사용자 초대 요청을 보냅니다.")
    @ApiErrorCodeExamples(PageErrorCode.class)
    @PostMapping("/share-page-invitations")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiPageResponseSpec<SharePageInvitationRequestCreateResponse>> inviteSharePage(
            @RequestBody @Validated SharePageInvitationRequestCreate sharePageInvitationRequestCreate,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    );

}

package com.linkmoa.source.domain.page.controller.spec;

import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.page.dto.request.PageCreateRequest;
import com.linkmoa.source.domain.page.dto.request.PageDeleteRequest;
import com.linkmoa.source.domain.dispatch.dto.request.SharePageInvitationRequestCreate;
import com.linkmoa.source.domain.page.dto.response.ApiPageResponseSpec;
import com.linkmoa.source.domain.dispatch.dto.response.SharePageInvitationRequestCreateResponse;
import com.linkmoa.source.domain.page.dto.response.PagesResponse;
import com.linkmoa.source.domain.page.dto.response.SharePageLeaveResponse;
import com.linkmoa.source.domain.page.error.PageErrorCode;
import com.linkmoa.source.global.dto.request.BaseRequest;
import com.linkmoa.source.global.swagger.ApiErrorCodeExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface PageApiSpecification {


    @Tag(name = "Post", description = "페이지 관련 API")
    @Operation(summary = "페이지 생성", description = "페이지를 생성합니다.(개인,공유)")
    @ApiErrorCodeExamples(PageErrorCode.class)
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiPageResponseSpec<Long>> createPage(
            @RequestBody @Validated PageCreateRequest pageCreateRequest,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    );

    @Tag(name = "Delete", description = "페이지 관련 API")
    @Operation(summary = "페이지 삭제", description = "페이지를 삭제합니다.(개인,공유)")
    @ApiErrorCodeExamples(PageErrorCode.class)
    @DeleteMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiPageResponseSpec<Long>> deletePage(
            @RequestBody @Validated PageDeleteRequest pageDeleteRequest,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    );

    @Tag(name = "Get", description = "페이지 관련 API")
    @Operation(summary = "모든 페이지 목록 조회", description = "사용자가 참여 중인 모든 페이지 목록 조회")
    @ApiErrorCodeExamples(PageErrorCode.class)
    @GetMapping()
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiPageResponseSpec<List<PagesResponse>>> getAllPages(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    );


    @Tag(name = "Post", description = "페이지 관련 API")
    @Operation(summary = "공유 페이지 탈퇴", description = "사용자가 공유 페이지를 탈퇴합니다.")
    @ApiErrorCodeExamples(PageErrorCode.class)
    @DeleteMapping("/leave")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiPageResponseSpec<SharePageLeaveResponse>> leaveSharePage(
            @RequestBody @Validated BaseRequest baseRequest,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    );





}

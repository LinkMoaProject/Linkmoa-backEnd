package com.linkmoa.source.domain.search.controller;

import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.directory.dto.response.ApiDirectoryResponseSpec;
import com.linkmoa.source.domain.directory.dto.response.DirectoryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface SearchApiSpecification {
    @Tag(name = "Search", description = "조회 관련 API")
    @Operation(summary = "디렉토리 또는 사이트 조회", description = "디렉토리 또는 사이트 조회합니다.")
    //@ApiErrorCodeExamples(DirectoryErrorCode.class)
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    ResponseEntity<ApiDirectoryResponseSpec<DirectoryResponse>> searchDirectoryAndSite(
            @RequestParam String title,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    );
}

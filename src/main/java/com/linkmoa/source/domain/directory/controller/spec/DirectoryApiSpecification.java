package com.linkmoa.source.domain.directory.controller.spec;

import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.directory.dto.request.DirectoryCreateRequestDto;
import com.linkmoa.source.domain.directory.dto.request.DirectoryUpdateRequestDto;
import com.linkmoa.source.domain.directory.dto.response.ApiDirectoryResponseSpec;
import com.linkmoa.source.domain.directory.error.DirectoryErrorCode;
import com.linkmoa.source.global.swagger.ApiErrorCodeExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

public interface DirectoryApiSpecification {


   /* @Tag(name = "Post",description = "디렉토리 관련 API")
    @Operation(summary = "디렉토리 생성",description = "디렉토리를 생성합니다.")
    @ApiErrorCodeExamples(DirectoryErrorCode.class)
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiDirectoryResponseSpec<Long>> saveDirectory(
            @RequestBody @Validated DirectoryCreateRequestDto directoryCreateRequestDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    );

    @Tag(name = "Delete",description = "디렉토리 관련 API")
    @Operation(summary = "디렉토리 삭제",description = "디렉토리를 삭제합니다.")
    @ApiErrorCodeExamples(DirectoryErrorCode.class)
    @DeleteMapping("/{directoryId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiDirectoryResponseSpec<Long>> deleteDirecotry(
            @PathVariable("directoryId") @NotBlank Long directoryId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    );


    @Tag(name = "Put",description = "디렉토리 관련 API")
    @Operation(summary = "디렉토리 수정",description = "디렉토리를 수정(이름,부모 디렉토리)합니다.")
    @ApiErrorCodeExamples(DirectoryErrorCode.class)
    @PutMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiDirectoryResponseSpec<DirectoryUpdateResponseDto>> updatedirecotry(
            @RequestBody DirectoryUpdateRequestDto directoryUpdateRequestDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    );*/
}

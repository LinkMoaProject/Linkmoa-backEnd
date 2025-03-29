package com.linkmoa.source.domain.directory.controller.spec;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.directory.dto.request.DirectoryChangeParentRequest;
import com.linkmoa.source.domain.directory.dto.request.DirectoryCreateRequest;
import com.linkmoa.source.domain.directory.dto.request.DirectoryDragAndDropRequest;
import com.linkmoa.source.domain.directory.dto.request.DirectoryIdRequest;
import com.linkmoa.source.domain.directory.dto.request.DirectoryPasteRequest;
import com.linkmoa.source.domain.directory.dto.request.DirectoryUpdateRequest;
import com.linkmoa.source.domain.directory.dto.response.ApiDirectoryResponseSpec;
import com.linkmoa.source.domain.directory.dto.response.DirectoryDragAndDropResponse;
import com.linkmoa.source.domain.directory.dto.response.DirectoryPasteResponse;
import com.linkmoa.source.domain.directory.dto.response.DirectoryResponse;
import com.linkmoa.source.domain.directory.error.DirectoryErrorCode;
import com.linkmoa.source.global.swagger.ApiErrorCodeExamples;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

public interface DirectoryApiSpecification {

	@Tag(name = "Directory", description = "디렉토리 관련 API")
	@Operation(summary = "디렉토리 생성", description = "디렉토리를 생성합니다.")
	@ApiErrorCodeExamples(DirectoryErrorCode.class)
	@PostMapping
	@PreAuthorize("isAuthenticated()")
	ResponseEntity<ApiDirectoryResponseSpec<Long>> createDirectory(
		@RequestBody @Validated DirectoryCreateRequest directoryCreateRequest,
		@AuthenticationPrincipal PrincipalDetails principalDetails
	);

	@Tag(name = "Directory", description = "디렉토리 관련 API")
	@Operation(summary = "디렉토리 삭제", description = "디렉토리를 삭제합니다.")
	@ApiErrorCodeExamples(DirectoryErrorCode.class)
	@DeleteMapping()
	@PreAuthorize("isAuthenticated()")
	ResponseEntity<ApiDirectoryResponseSpec<Long>> deleteDirectory(
		@RequestBody @Validated DirectoryIdRequest directoryIdRequestDto,
		@AuthenticationPrincipal PrincipalDetails principalDetails
	);

	@Tag(name = "Directory", description = "디렉토리 관련 API")
	@Operation(summary = "디렉토리 수정", description = "디렉토리를 수정(이름,설명)합니다.")
	@ApiErrorCodeExamples(DirectoryErrorCode.class)
	@PutMapping
	@PreAuthorize("isAuthenticated()")
	ResponseEntity<ApiDirectoryResponseSpec<Long>> updateDirectory(
		@RequestBody DirectoryUpdateRequest directoryUpdateRequest,
		@AuthenticationPrincipal PrincipalDetails principalDetails
	);

	@Tag(name = "Directory", description = "디렉토리 관련 API")
	@Operation(summary = "디렉토리 위치 이동", description = "같은 부모 디렉토리를 가진 디렉토리를 다른 디렉토리로 이동(드래그 앤 드롭)")
	@ApiErrorCodeExamples(DirectoryErrorCode.class)
	@PutMapping("/move")
	@PreAuthorize("isAuthenticated()")
	ResponseEntity<ApiDirectoryResponseSpec<Long>> moveDirectory(
		@RequestBody @Validated DirectoryChangeParentRequest directoryMoveRequest,
		@AuthenticationPrincipal PrincipalDetails principalDetails
	);

	@Tag(name = "Directory", description = "디렉토리 관련 API")
	@Operation(summary = "디렉토리 상세 조회", description = "디렉토리 클릭 시, 디렉토리의 상세 정보(이름 및 소개글)와 해당 디렉토리 내 포함된 하위 디렉토리 및 사이트 조회")
	@ApiErrorCodeExamples(DirectoryErrorCode.class)
	@GetMapping("/details")
	@PreAuthorize("isAuthenticated()")
	ResponseEntity<ApiDirectoryResponseSpec<DirectoryResponse>> getDirectory(
		@RequestBody @Validated DirectoryIdRequest directoryIdRequest,
		@AuthenticationPrincipal PrincipalDetails principalDetails
	);

	@Tag(name = "Directory", description = "디렉토리 관련 API")
	@Operation(summary = "디렉토리 붙여넣기 기능", description = "지정한 디렉토리를 복사하여 선택한 위치에 붙여넣습니다.")
	@ApiErrorCodeExamples(DirectoryErrorCode.class)
	@PostMapping("/paste")
	@PreAuthorize("isAuthenticated()")
	ResponseEntity<ApiDirectoryResponseSpec<DirectoryPasteResponse>> pasteDirectory(
		@RequestBody DirectoryPasteRequest directoryPasteRequest,
		@AuthenticationPrincipal PrincipalDetails principalDetails
	);

	@Tag(name = "Directory", description = "디렉토리 관련 API")
	@Operation(summary = "디렉토리 및 사이트의 드래그 앤 드랍 기능 ", description = "드래그 앤 드롭을 통해 동일한 디렉토리 내에서 디렉토리 및 사이트의 정렬 순서를 자유롭게 변경 가능")
	@ApiErrorCodeExamples(DirectoryErrorCode.class)
	@PutMapping("/drag-and-drop")
	@PreAuthorize("isAuthenticated()")
	ResponseEntity<ApiDirectoryResponseSpec<DirectoryDragAndDropResponse>> dragAndDropDirectoryOrSite(
		@RequestBody @Validated DirectoryDragAndDropRequest directoryDragAndDropRequest,
		@AuthenticationPrincipal PrincipalDetails principalDetails
	);

}

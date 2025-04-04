package com.linkmoa.source.domain.directory.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.directory.dto.request.DirectoryChangeParentDto;
import com.linkmoa.source.domain.directory.dto.request.DirectoryCreateDto;
import com.linkmoa.source.domain.directory.dto.request.DirectoryDragAndDropDto;
import com.linkmoa.source.domain.directory.dto.request.DirectoryIdDto;
import com.linkmoa.source.domain.directory.dto.request.DirectoryPasteDto;
import com.linkmoa.source.domain.directory.dto.request.DirectoryUpdateDto;
import com.linkmoa.source.domain.directory.service.DirectoryService;
import com.linkmoa.source.global.spec.ApiResponseSpec;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/directories")
@Slf4j
public class DirectoryApiController {

	private final DirectoryService directoryService;

	@PostMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiResponseSpec<Long>> createDirectory(
		@RequestBody @Validated DirectoryCreateDto.Request directoryCreateRequest,
		@AuthenticationPrincipal PrincipalDetails principalDetails) {
		return ResponseEntity.ok().body(ApiResponseSpec.success(
			HttpStatus.OK,
			"디렉토리 생성에 성공했습니다.",
			directoryService.createDirectory(
				directoryCreateRequest, principalDetails)
		));
	}

	@PutMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiResponseSpec<Long>> updateDirectory(
		@RequestBody DirectoryUpdateDto.Request directoryUpdateRequest,
		@AuthenticationPrincipal PrincipalDetails principalDetails
	) {
		return ResponseEntity.ok().body(ApiResponseSpec.success(
			HttpStatus.OK,
			"Directory 수정(이름,설명)에 성공했습니다.",
			directoryService.updateDirectory(
				directoryUpdateRequest, principalDetails)
		));
	}

	@DeleteMapping()
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiResponseSpec<Long>> deleteDirectory(
		@RequestBody @Validated DirectoryIdDto.Request directoryIdRequestDto,
		@AuthenticationPrincipal PrincipalDetails principalDetails
	) {
		return ResponseEntity.ok().body(ApiResponseSpec.success(
			HttpStatus.OK,
			"Directory 삭제에 성공했습니다.",
			directoryService.deleteDirectory(directoryIdRequestDto,
				principalDetails)
		));
	}

	@PutMapping("/move")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiResponseSpec<Long>> changeParentDirectory(
		@RequestBody @Validated DirectoryChangeParentDto.Request directoryChangeParentRequest,
		@AuthenticationPrincipal PrincipalDetails principalDetails
	) {
		return ResponseEntity.ok().body(ApiResponseSpec.success(
			HttpStatus.OK,
			"디렉토리를 다른 디렉토리로 이동시켰습니다.",
			directoryService.changeParentDirectory(
				directoryChangeParentRequest,
				principalDetails)
		));
	}

	@GetMapping("/details")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiResponseSpec<DirectoryIdDto.Response>> getDirectory(
		@RequestBody @Validated DirectoryIdDto.Request directoryIdRequest,
		@AuthenticationPrincipal PrincipalDetails principalDetails) {
		return ResponseEntity.ok().body(ApiResponseSpec.success(
			HttpStatus.OK,
			"Directory 클릭 시, 해당 디렉토리 내에 사이트 및 디렉토리를 조회했습니다.",
			directoryService.findDirectoryDetails(
				directoryIdRequest, principalDetails)
		));
	}

	@PostMapping("/paste")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiResponseSpec<DirectoryPasteDto.Response>> pasteDirectory(
		@RequestBody @Validated DirectoryPasteDto.Request directoryPasteRequest,
		@AuthenticationPrincipal PrincipalDetails principalDetails) {

		return ResponseEntity.ok().body(ApiResponseSpec.success(
			HttpStatus.OK,
			"Directory 붙여넣기에 성공했습니다.",
			directoryService.pasteDirectory(
				directoryPasteRequest, principalDetails)
		));
	}

	@PutMapping("/drag-and-drop")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiResponseSpec<DirectoryDragAndDropDto.Response>> dragAndDropDirectoryOrSite(
		@RequestBody @Validated DirectoryDragAndDropDto.Request directoryDragAndDropRequest,
		@AuthenticationPrincipal PrincipalDetails principalDetails) {

		return ResponseEntity.ok().body(ApiResponseSpec.success(
			HttpStatus.OK,
			"Drag and Drop를 수행했습니다.",
			directoryService.dragAndDropDirectoryOrSite(directoryDragAndDropRequest, principalDetails)
		));
	}

}

package com.linkmoa.source.domain.directory.controller;

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
import com.linkmoa.source.domain.directory.dto.request.DirectoryChangeParentRequest;
import com.linkmoa.source.domain.directory.dto.request.DirectoryCreateRequest;
import com.linkmoa.source.domain.directory.dto.request.DirectoryDragAndDropRequest;
import com.linkmoa.source.domain.directory.dto.request.DirectoryIdRequest;
import com.linkmoa.source.domain.directory.dto.request.DirectoryPasteRequest;
import com.linkmoa.source.domain.directory.dto.request.DirectoryUpdateRequest;
import com.linkmoa.source.domain.directory.dto.response.DirectoryDragAndDropResponse;
import com.linkmoa.source.domain.directory.dto.response.DirectoryPasteResponse;
import com.linkmoa.source.domain.directory.dto.response.DirectoryResponse;
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
		@RequestBody @Validated DirectoryCreateRequest directoryCreateRequest,
		@AuthenticationPrincipal PrincipalDetails principalDetails) {
		ApiResponseSpec<Long> response = directoryService.createDirectory(
			directoryCreateRequest, principalDetails);
		return ResponseEntity.ok().body(response);
	}

	@PutMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiResponseSpec<Long>> updateDirectory(
		@RequestBody DirectoryUpdateRequest directoryUpdateRequest,
		@AuthenticationPrincipal PrincipalDetails principalDetails
	) {
		ApiResponseSpec<Long> response = directoryService.updateDirectory(
			directoryUpdateRequest, principalDetails);
		return ResponseEntity.ok().body(response);
	}

	@DeleteMapping()
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiResponseSpec<Long>> deleteDirectory(
		@RequestBody @Validated DirectoryIdRequest directoryIdRequestDto,
		@AuthenticationPrincipal PrincipalDetails principalDetails
	) {
		ApiResponseSpec<Long> response = directoryService.deleteDirectory(directoryIdRequestDto,
			principalDetails);
		return ResponseEntity.ok().body(response);
	}

	@PutMapping("/move")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiResponseSpec<Long>> changeParentDirectory(
		@RequestBody @Validated DirectoryChangeParentRequest directoryChangeParentRequest,
		@AuthenticationPrincipal PrincipalDetails principalDetails
	) {
		ApiResponseSpec<Long> response = directoryService.changeParentDirectory(
			directoryChangeParentRequest,
			principalDetails);
		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/details")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiResponseSpec<DirectoryResponse>> getDirectory(
		@RequestBody @Validated DirectoryIdRequest directoryIdRequest,
		@AuthenticationPrincipal PrincipalDetails principalDetails) {
		ApiResponseSpec<DirectoryResponse> response = directoryService.findDirectoryDetails(
			directoryIdRequest, principalDetails);
		return ResponseEntity.ok().body(response);
	}

	@PostMapping("/paste")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiResponseSpec<DirectoryPasteResponse>> pasteDirectory(
		@RequestBody @Validated DirectoryPasteRequest directoryPasteRequest,
		@AuthenticationPrincipal PrincipalDetails principalDetails) {
		ApiResponseSpec<DirectoryPasteResponse> response = directoryService.pasteDirectory(
			directoryPasteRequest, principalDetails);

		return ResponseEntity.ok().body(response);
	}

	@PutMapping("/drag-and-drop")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiResponseSpec<DirectoryDragAndDropResponse>> dragAndDropDirectoryOrSite(
		@RequestBody @Validated DirectoryDragAndDropRequest directoryDragAndDropRequest,
		@AuthenticationPrincipal PrincipalDetails principalDetails) {
		ApiResponseSpec<DirectoryDragAndDropResponse> response =
			directoryService.dragAndDropDirectoryOrSite(directoryDragAndDropRequest, principalDetails);

		return ResponseEntity.ok().body(response);
	}

}

package com.linkmoa.source.domain.directory.controller.impl;

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
import com.linkmoa.source.domain.directory.dto.response.ApiDirectoryResponseSpec;
import com.linkmoa.source.domain.directory.dto.response.DirectoryDragAndDropResponse;
import com.linkmoa.source.domain.directory.dto.response.DirectoryPasteResponse;
import com.linkmoa.source.domain.directory.dto.response.DirectoryResponse;
import com.linkmoa.source.domain.directory.service.DirectoryService;

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
	public ResponseEntity<ApiDirectoryResponseSpec<Long>> createDirectory(
		@RequestBody @Validated DirectoryCreateRequest directoryCreateRequest,
		@AuthenticationPrincipal PrincipalDetails principalDetails) {
		ApiDirectoryResponseSpec<Long> createDirectoryResponse = directoryService.createDirectory(
			directoryCreateRequest, principalDetails);
		return ResponseEntity.ok().body(createDirectoryResponse);
	}

	@PutMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiDirectoryResponseSpec<Long>> updateDirectory(
		@RequestBody DirectoryUpdateRequest directoryUpdateRequest,
		@AuthenticationPrincipal PrincipalDetails principalDetails
	) {
		ApiDirectoryResponseSpec<Long> updateDirectoryResponse = directoryService.updateDirectory(
			directoryUpdateRequest, principalDetails);
		return ResponseEntity.ok().body(updateDirectoryResponse);
	}

	@DeleteMapping()
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiDirectoryResponseSpec<Long>> deleteDirectory(
		@RequestBody @Validated DirectoryIdRequest directoryIdRequestDto,
		@AuthenticationPrincipal PrincipalDetails principalDetails
	) {
		ApiDirectoryResponseSpec<Long> deleteDirectoryResponse = directoryService.deleteDirectory(directoryIdRequestDto,
			principalDetails);
		return ResponseEntity.ok().body(deleteDirectoryResponse);
	}

	@PutMapping("/move")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiDirectoryResponseSpec<Long>> changeParentDirectory(
		@RequestBody @Validated DirectoryChangeParentRequest directoryChangeParentRequest,
		@AuthenticationPrincipal PrincipalDetails principalDetails
	) {
		ApiDirectoryResponseSpec<Long> moveDirectoryResponse = directoryService.changeParentDirectory(
			directoryChangeParentRequest,
			principalDetails);
		return ResponseEntity.ok().body(moveDirectoryResponse);
	}

	@GetMapping("/details")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiDirectoryResponseSpec<DirectoryResponse>> getDirectory(
		@RequestBody @Validated DirectoryIdRequest directoryIdRequest,
		@AuthenticationPrincipal PrincipalDetails principalDetails) {
		ApiDirectoryResponseSpec<DirectoryResponse> directoryResponse = directoryService.findDirectoryDetails(
			directoryIdRequest, principalDetails);
		return ResponseEntity.ok().body(directoryResponse);
	}

	@PostMapping("/paste")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiDirectoryResponseSpec<DirectoryPasteResponse>> pasteDirectory(
		@RequestBody @Validated DirectoryPasteRequest directoryPasteRequest,
		@AuthenticationPrincipal PrincipalDetails principalDetails) {
		ApiDirectoryResponseSpec<DirectoryPasteResponse> directoryPasteResponse = directoryService.pasteDirectory(
			directoryPasteRequest, principalDetails);

		return ResponseEntity.ok().body(directoryPasteResponse);
	}

	@PutMapping("/drag-and-drop")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiDirectoryResponseSpec<DirectoryDragAndDropResponse>> dragAndDropDirectoryOrSite(
		@RequestBody @Validated DirectoryDragAndDropRequest directoryDragAndDropRequest,
		@AuthenticationPrincipal PrincipalDetails principalDetails) {
		ApiDirectoryResponseSpec<DirectoryDragAndDropResponse> directoryDragAndDropResponseApiDirectoryResponse =
			directoryService.dragAndDropDirectoryOrSite(directoryDragAndDropRequest, principalDetails);

		return ResponseEntity.ok().body(directoryDragAndDropResponseApiDirectoryResponse);
	}

}

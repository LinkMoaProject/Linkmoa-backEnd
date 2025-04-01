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
		ApiResponseSpec<Long> response = directoryService.createDirectory(
			directoryCreateRequest, principalDetails);
		return ResponseEntity.ok().body(response);
	}

	@PutMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiResponseSpec<Long>> updateDirectory(
		@RequestBody DirectoryUpdateDto.Request directoryUpdateRequest,
		@AuthenticationPrincipal PrincipalDetails principalDetails
	) {
		ApiResponseSpec<Long> response = directoryService.updateDirectory(
			directoryUpdateRequest, principalDetails);
		return ResponseEntity.ok().body(response);
	}

	@DeleteMapping()
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiResponseSpec<Long>> deleteDirectory(
		@RequestBody @Validated DirectoryIdDto.Request directoryIdRequestDto,
		@AuthenticationPrincipal PrincipalDetails principalDetails
	) {
		ApiResponseSpec<Long> response = directoryService.deleteDirectory(directoryIdRequestDto,
			principalDetails);
		return ResponseEntity.ok().body(response);
	}

	@PutMapping("/move")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiResponseSpec<Long>> changeParentDirectory(
		@RequestBody @Validated DirectoryChangeParentDto.Request directoryChangeParentRequest,
		@AuthenticationPrincipal PrincipalDetails principalDetails
	) {
		ApiResponseSpec<Long> response = directoryService.changeParentDirectory(
			directoryChangeParentRequest,
			principalDetails);
		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/details")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiResponseSpec<DirectoryIdDto.Response>> getDirectory(
		@RequestBody @Validated DirectoryIdDto.Request directoryIdRequest,
		@AuthenticationPrincipal PrincipalDetails principalDetails) {
		ApiResponseSpec<DirectoryIdDto.Response> response = directoryService.findDirectoryDetails(
			directoryIdRequest, principalDetails);
		return ResponseEntity.ok().body(response);
	}

	@PostMapping("/paste")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiResponseSpec<DirectoryPasteDto.Response>> pasteDirectory(
		@RequestBody @Validated DirectoryPasteDto.Request directoryPasteRequest,
		@AuthenticationPrincipal PrincipalDetails principalDetails) {
		ApiResponseSpec<DirectoryPasteDto.Response> response = directoryService.pasteDirectory(
			directoryPasteRequest, principalDetails);

		return ResponseEntity.ok().body(response);
	}

	@PutMapping("/drag-and-drop")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiResponseSpec<DirectoryDragAndDropDto.Response>> dragAndDropDirectoryOrSite(
		@RequestBody @Validated DirectoryDragAndDropDto.Request directoryDragAndDropRequest,
		@AuthenticationPrincipal PrincipalDetails principalDetails) {
		ApiResponseSpec<DirectoryDragAndDropDto.Response> response =
			directoryService.dragAndDropDirectoryOrSite(directoryDragAndDropRequest, principalDetails);

		return ResponseEntity.ok().body(response);
	}

}

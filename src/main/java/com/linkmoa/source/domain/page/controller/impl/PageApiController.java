package com.linkmoa.source.domain.page.controller.impl;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.page.dto.request.PageCreateRequest;
import com.linkmoa.source.domain.page.dto.request.PageDeleteRequest;
import com.linkmoa.source.domain.page.dto.response.ApiPageResponseSpec;
import com.linkmoa.source.domain.page.dto.response.PageDetailsResponse;
import com.linkmoa.source.domain.page.dto.response.PageResponse;
import com.linkmoa.source.domain.page.dto.response.SharePageLeaveResponse;
import com.linkmoa.source.domain.page.service.PageService;
import com.linkmoa.source.global.dto.request.BaseRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/page")
public class PageApiController {

	private final PageService pageService;

	@PostMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiPageResponseSpec<Long>> createPage(
		@RequestBody @Validated PageCreateRequest pageCreateRequest,
		@AuthenticationPrincipal PrincipalDetails principalDetails) {
		ApiPageResponseSpec<Long> createPageResponse = pageService.createSharedPage(pageCreateRequest,
			principalDetails);

		return ResponseEntity.ok().body(createPageResponse);
	}

	@DeleteMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiPageResponseSpec<Long>> deletePage(
		@RequestBody @Validated PageDeleteRequest pageDeleteRequest,
		@AuthenticationPrincipal PrincipalDetails principalDetails) {
		ApiPageResponseSpec<Long> deletePageResponse = pageService.deletePage(pageDeleteRequest, principalDetails);

		return ResponseEntity.ok().body(deletePageResponse);
	}

	@GetMapping()
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiPageResponseSpec<List<PageResponse>>> getAllPages(
		@AuthenticationPrincipal PrincipalDetails principalDetails) {

		ApiPageResponseSpec<List<PageResponse>> allPages = pageService.findAllPages(principalDetails);

		return ResponseEntity.ok().body(allPages);

	}

	@DeleteMapping("/leave")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiPageResponseSpec<SharePageLeaveResponse>> leaveSharePage(
		@RequestBody @Validated BaseRequest baseRequest,
		@AuthenticationPrincipal PrincipalDetails principalDetails) {
		ApiPageResponseSpec<SharePageLeaveResponse> sharePageLeaveResponse = pageService.leaveSharePage(baseRequest,
			principalDetails);

		return ResponseEntity.ok().body(sharePageLeaveResponse);
	}

	@GetMapping("/details")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiPageResponseSpec<PageDetailsResponse>> getPageDetails(
		@RequestBody @Validated BaseRequest baseRequest,
		@AuthenticationPrincipal PrincipalDetails principalDetails) {
		ApiPageResponseSpec<PageDetailsResponse> pageDetailsResponse = pageService.getPageMain(baseRequest,
			principalDetails);
		return ResponseEntity.ok().body(pageDetailsResponse);
	}

	@GetMapping("/login")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiPageResponseSpec<PageDetailsResponse>> loadPersonalPageMain(
		@AuthenticationPrincipal PrincipalDetails principalDetails) {
		ApiPageResponseSpec<PageDetailsResponse> pageDetailsResponse = pageService.loadPersonalPageMain(
			principalDetails);
		return ResponseEntity.ok().body(pageDetailsResponse);
	}

}

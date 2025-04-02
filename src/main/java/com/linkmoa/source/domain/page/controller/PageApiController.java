package com.linkmoa.source.domain.page.controller;

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
import com.linkmoa.source.domain.page.dto.request.PageCreateDto;
import com.linkmoa.source.domain.page.dto.request.PageDeleteDto;
import com.linkmoa.source.domain.page.dto.response.PageDetailsResponse;
import com.linkmoa.source.domain.page.dto.response.PageResponse;
import com.linkmoa.source.domain.page.dto.response.SharePageLeaveResponse;
import com.linkmoa.source.domain.page.service.PageService;
import com.linkmoa.source.global.dto.request.BaseRequest;
import com.linkmoa.source.global.spec.ApiResponseSpec;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/page")
public class PageApiController {

	private final PageService pageService;

	@PostMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiResponseSpec<Long>> createPage(
		@RequestBody @Validated PageCreateDto.Request pageCreateDto,
		@AuthenticationPrincipal PrincipalDetails principalDetails) {
		ApiResponseSpec<Long> createPageResponse = pageService.createSharedPage(pageCreateDto,
			principalDetails);

		return ResponseEntity.ok().body(createPageResponse);
	}

	@DeleteMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiResponseSpec<Long>> deletePage(
		@RequestBody @Validated PageDeleteDto.Request pageDeleteDto,
		@AuthenticationPrincipal PrincipalDetails principalDetails) {
		ApiResponseSpec<Long> response = pageService.deletePage(pageDeleteDto, principalDetails);

		return ResponseEntity.ok().body(response);
	}

	@GetMapping()
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiResponseSpec<List<PageResponse>>> getAllPages(
		@AuthenticationPrincipal PrincipalDetails principalDetails) {

		ApiResponseSpec<List<PageResponse>> response = pageService.findAllPages(principalDetails);

		return ResponseEntity.ok().body(response);

	}

	@DeleteMapping("/leave")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiResponseSpec<SharePageLeaveResponse>> leaveSharePage(
		@RequestBody @Validated BaseRequest baseRequest,
		@AuthenticationPrincipal PrincipalDetails principalDetails) {
		ApiResponseSpec<SharePageLeaveResponse> response = pageService.leaveSharePage(baseRequest,
			principalDetails);

		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/details")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiResponseSpec<PageDetailsResponse>> getPageDetails(
		@RequestBody @Validated BaseRequest baseRequest,
		@AuthenticationPrincipal PrincipalDetails principalDetails) {
		ApiResponseSpec<PageDetailsResponse> response = pageService.getPageMain(baseRequest,
			principalDetails);
		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/login")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiResponseSpec<PageDetailsResponse>> loadPersonalPageMain(
		@AuthenticationPrincipal PrincipalDetails principalDetails) {
		ApiResponseSpec<PageDetailsResponse> response = pageService.loadPersonalPageMain(
			principalDetails);
		return ResponseEntity.ok().body(response);
	}

}

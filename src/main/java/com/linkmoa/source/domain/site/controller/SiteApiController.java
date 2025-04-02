package com.linkmoa.source.domain.site.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.site.dto.request.SiteCreateDto;
import com.linkmoa.source.domain.site.dto.request.SiteDeleteDto;
import com.linkmoa.source.domain.site.dto.request.SiteMoveRequestDto;
import com.linkmoa.source.domain.site.dto.request.SiteUpdateRequestDto;
import com.linkmoa.source.domain.site.service.SiteService;
import com.linkmoa.source.global.spec.ApiResponseSpec;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sites")
public class SiteApiController {

	private final SiteService siteService;

	@PostMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiResponseSpec<Long>> saveSite(
		@RequestBody @Validated SiteCreateDto.Request siteCreateDto,
		@AuthenticationPrincipal PrincipalDetails principalDetails) {
		ApiResponseSpec<Long> response = siteService.createSite(siteCreateDto, principalDetails);
		return ResponseEntity.ok().body(response);
	}

	@PutMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiResponseSpec<Long>> updateSite(
		@RequestBody @Validated SiteUpdateRequestDto siteCreateRequestDto,
		@AuthenticationPrincipal PrincipalDetails principalDetails) {
		ApiResponseSpec<Long> response = siteService.updateSite(siteCreateRequestDto, principalDetails);
		return ResponseEntity.ok().body(response);
	}

	@DeleteMapping()
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiResponseSpec<Long>> deleteSite(
		@RequestBody @Validated SiteDeleteDto.Request siteDeleteDto,
		@AuthenticationPrincipal PrincipalDetails principalDetails) {
		ApiResponseSpec<Long> response = siteService.deleteSite(siteDeleteDto, principalDetails);
		return ResponseEntity.ok().body(response);
	}

	@PutMapping("/move")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiResponseSpec<Long>> moveSite(
		@RequestBody @Validated SiteMoveRequestDto siteMoveRequestDto,
		@AuthenticationPrincipal PrincipalDetails principalDetails) {
		ApiResponseSpec<Long> response = siteService.moveSite(siteMoveRequestDto, principalDetails);
		return ResponseEntity.ok().body(response);
	}
}

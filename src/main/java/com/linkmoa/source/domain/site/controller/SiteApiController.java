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
import com.linkmoa.source.domain.site.dto.request.SiteCreateRequestDto;
import com.linkmoa.source.domain.site.dto.request.SiteDeleteRequestDto;
import com.linkmoa.source.domain.site.dto.request.SiteMoveRequestDto;
import com.linkmoa.source.domain.site.dto.request.SiteUpdateRequestDto;
import com.linkmoa.source.domain.site.dto.response.ApiSiteResponse;
import com.linkmoa.source.domain.site.service.SiteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sites")
public class SiteApiController {

	private final SiteService siteService;

	@PostMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiSiteResponse<Long>> saveSite(
		@RequestBody @Validated SiteCreateRequestDto siteCreateRequestDto,
		@AuthenticationPrincipal PrincipalDetails principalDetails) {
		ApiSiteResponse<Long> siteCreateResponse = siteService.createSite(siteCreateRequestDto, principalDetails);
		return ResponseEntity.ok().body(siteCreateResponse);
	}

	@PutMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiSiteResponse<Long>> updateSite(
		@RequestBody @Validated SiteUpdateRequestDto siteCreateRequestDto,
		@AuthenticationPrincipal PrincipalDetails principalDetails) {
		ApiSiteResponse<Long> siteUpdateResponse = siteService.updateSite(siteCreateRequestDto, principalDetails);
		return ResponseEntity.ok().body(siteUpdateResponse);
	}

	@DeleteMapping()
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiSiteResponse<Long>> deleteSite(
		@RequestBody @Validated SiteDeleteRequestDto siteDeleteRequestDto,
		@AuthenticationPrincipal PrincipalDetails principalDetails) {
		ApiSiteResponse<Long> siteDeleteResponse = siteService.deleteSite(siteDeleteRequestDto, principalDetails);
		return ResponseEntity.ok().body(siteDeleteResponse);
	}

	@PutMapping("/move")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiSiteResponse<Long>> moveSite(
		@RequestBody @Validated SiteMoveRequestDto siteMoveRequestDto,
		@AuthenticationPrincipal PrincipalDetails principalDetails) {
		ApiSiteResponse<Long> siteMoveResponse = siteService.moveSite(siteMoveRequestDto, principalDetails);
		return ResponseEntity.ok().body(siteMoveResponse);
	}
}

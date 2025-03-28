package com.linkmoa.source.domain.site.controller.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.site.controller.spec.SiteApiSpecification;
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
public class SiteApiController implements SiteApiSpecification {

	private final SiteService siteService;

	public ResponseEntity<ApiSiteResponse<Long>> saveSite(
		SiteCreateRequestDto siteCreateRequestDto,
		PrincipalDetails principalDetails) {
		ApiSiteResponse<Long> siteCreateResponse = siteService.createSite(siteCreateRequestDto, principalDetails);
		return ResponseEntity.ok().body(siteCreateResponse);
	}

	public ResponseEntity<ApiSiteResponse<Long>> updateSite(
		SiteUpdateRequestDto siteCreateRequestDto,
		PrincipalDetails principalDetails) {
		ApiSiteResponse<Long> siteUpdateResponse = siteService.updateSite(siteCreateRequestDto, principalDetails);
		return ResponseEntity.ok().body(siteUpdateResponse);
	}

	public ResponseEntity<ApiSiteResponse<Long>> deleteSite(
		SiteDeleteRequestDto siteDeleteRequestDto,
		PrincipalDetails principalDetails) {
		ApiSiteResponse<Long> siteDeleteResponse = siteService.deleteSite(siteDeleteRequestDto, principalDetails);
		return ResponseEntity.ok().body(siteDeleteResponse);
	}

	public ResponseEntity<ApiSiteResponse<Long>> moveSite(
		SiteMoveRequestDto siteMoveRequestDto,
		PrincipalDetails principalDetails) {
		ApiSiteResponse<Long> siteMoveResponse = siteService.moveSite(siteMoveRequestDto, principalDetails);
		return ResponseEntity.ok().body(siteMoveResponse);
	}
}

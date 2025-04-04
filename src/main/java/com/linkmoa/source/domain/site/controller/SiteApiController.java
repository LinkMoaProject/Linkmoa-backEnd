package com.linkmoa.source.domain.site.controller;

import org.springframework.http.HttpStatus;
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
		return ResponseEntity.ok().body(ApiResponseSpec.success(
			HttpStatus.OK,
			"site 생성에 성공했습니다.",
			siteService.createSite(siteCreateDto, principalDetails)
		));
	}

	@PutMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiResponseSpec<Long>> updateSite(
		@RequestBody @Validated SiteUpdateRequestDto siteCreateRequestDto,
		@AuthenticationPrincipal PrincipalDetails principalDetails) {
		return ResponseEntity.ok().body(ApiResponseSpec.success(
			HttpStatus.OK,
			"site 수정(이름,url)에 성공했습니다.",
			siteService.updateSite(siteCreateRequestDto, principalDetails)
		));
	}

	@DeleteMapping()
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiResponseSpec<Long>> deleteSite(
		@RequestBody @Validated SiteDeleteDto.Request siteDeleteDto,
		@AuthenticationPrincipal PrincipalDetails principalDetails) {
		return ResponseEntity.ok().body(ApiResponseSpec.success(
			HttpStatus.OK,
			"site 삭제에 성공했습니다.",
			siteService.deleteSite(siteDeleteDto, principalDetails)
		));
	}

	@PutMapping("/move")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiResponseSpec<Long>> moveSite(
		@RequestBody @Validated SiteMoveRequestDto siteMoveRequestDto,
		@AuthenticationPrincipal PrincipalDetails principalDetails) {
		return ResponseEntity.ok().body(ApiResponseSpec.success(
			HttpStatus.OK,
			"site의 위치를 다른 directory로 위치 이동에 성공했습니다.",
			siteService.moveSite(siteMoveRequestDto, principalDetails)
		));
	}
}

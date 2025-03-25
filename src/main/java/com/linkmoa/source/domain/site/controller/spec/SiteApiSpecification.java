package com.linkmoa.source.domain.site.controller.spec;

import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.site.dto.request.SiteCreateRequestDto;
import com.linkmoa.source.domain.site.dto.request.SiteDeleteRequestDto;
import com.linkmoa.source.domain.site.dto.request.SiteMoveRequestDto;
import com.linkmoa.source.domain.site.dto.request.SiteUpdateRequestDto;
import com.linkmoa.source.domain.site.dto.response.ApiSiteResponse;
import com.linkmoa.source.domain.site.error.SiteErrorCode;
import com.linkmoa.source.global.swagger.ApiErrorCodeExamples;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface SiteApiSpecification {

	@Tag(name = "Site", description = "사이트 관련 API")
	@Operation(summary = "사이트 저장", description = "사이트를 특정 디렉토리에 생성합니다.")
	@ApiErrorCodeExamples(SiteErrorCode.class)
	@PostMapping
	@PreAuthorize("isAuthenticated()")
	ResponseEntity<ApiSiteResponse<Long>> saveSite(
		@RequestBody @Validated SiteCreateRequestDto siteCreateRequestDto,
		@AuthenticationPrincipal PrincipalDetails principalDetails
	);

	@Tag(name = "Site", description = "사이트 관련 API")
	@Operation(summary = "사이트 정보 수정", description = "사이트를 정보(이름,url)을 수정합니다.")
	@ApiErrorCodeExamples(SiteErrorCode.class)
	@PutMapping
	@PreAuthorize("isAuthenticated()")
	ResponseEntity<ApiSiteResponse<Long>> updateSite(
		@RequestBody @Validated SiteUpdateRequestDto siteCreateRequestDto,
		@AuthenticationPrincipal PrincipalDetails principalDetails
	);

	@Tag(name = "Site", description = "사이트 관련 API")
	@Operation(summary = "사이트 삭제", description = "사이트를 삭제합니다.")
	@ApiErrorCodeExamples(SiteErrorCode.class)
	@DeleteMapping()
	ResponseEntity<ApiSiteResponse<Long>> deleteSite(
		@RequestBody @Validated SiteDeleteRequestDto siteDeleteRequestDto,
		@AuthenticationPrincipal PrincipalDetails principalDetails
	);

	@Tag(name = "Site", description = "사이트 관련 API")
	@Operation(summary = "사이트 위치 이동", description = "사이트의 위치를 다른 디렉토리로 이동 (드래그 앤 스탑)")
	@ApiErrorCodeExamples(SiteErrorCode.class)
	@PutMapping("/move")
	@PreAuthorize("isAuthenticated()")
	ResponseEntity<ApiSiteResponse<Long>> moveSite(
		@RequestBody @Validated SiteMoveRequestDto siteMoveRequestDto,
		@AuthenticationPrincipal PrincipalDetails principalDetails
	);

}

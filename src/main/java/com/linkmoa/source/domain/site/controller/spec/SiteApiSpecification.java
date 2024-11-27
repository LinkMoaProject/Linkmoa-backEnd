package com.linkmoa.source.domain.site.controller.spec;

import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.site.dto.request.SiteCreateRequestDto;
import com.linkmoa.source.domain.site.dto.response.ApiSiteResponse;
import com.linkmoa.source.domain.site.error.SiteErrorCode;
import com.linkmoa.source.global.swagger.ApiErrorCodeExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface SiteApiSpecification {


    @Tag(name="Post",description = "사이트 관련 API")
    @Operation(summary = "사이트 저장",description = "사이트를 특정 디렉토리에 생성합니다.")
    @ApiErrorCodeExamples(SiteErrorCode.class)
    @PostMapping
    ResponseEntity<ApiSiteResponse<Long>> saveSite(
            @RequestBody @Validated SiteCreateRequestDto siteCreateRequestDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    );

/*

    @Tag(name="Post",description = "사이트 관련 API")
    @Operation(summary = "사이트 저장",description = "사이트를 저장합니다.")
    @ApiErrorCodeExamples(SiteErrorCode.class)
    @PostMapping
    ResponseEntity<ApiSiteResponse<Long>> saveSite(
            @RequestBody @Validated SiteCreateRequestDto siteCreateRequestDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    );


    @Tag(name="Delete",description = "사이트 관련 API")
    @Operation(summary = "사이트 삭제",description = "사이트를 삭제합니다.")
    @ApiErrorCodeExamples(SiteErrorCode.class)
    @DeleteMapping("/{siteId}")
    ResponseEntity<ApiSiteResponse<Long>> deleteSite(
            @PathVariable @NotBlank Long siteId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    );

    @Tag(name = "Put",description = "사이트 관련 API")
    @Operation(summary = "사이트 정보 수정",description = "사이트 정보(이름,url)를 수정합니다.")
    @ApiErrorCodeExamples(SiteErrorCode.class)
    @PutMapping
    ResponseEntity<ApiSiteResponse<SiteGetResponseDto>> updateSite(
            @RequestBody @Validated SiteUpdateRequestDto siteUpdateRequestDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    );

    @Tag(name="Get",description = "사이트 관련 API")
    @Operation(summary = "사이트 정보 조희",description = "1개의 사이트 정보(이름,url)를 조회합니다.")
    @ApiErrorCodeExamples(SiteErrorCode.class)
    @GetMapping("/{siteId}")
    ResponseEntity<ApiSiteResponse<SiteGetResponseDto>> getSite(
            @PathVariable @NotBlank Long siteId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    );


*/

}

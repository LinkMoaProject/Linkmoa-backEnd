package com.linkmoa.source.domain.site.controller.impl;


import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.site.controller.spec.SiteApiSpecification;
import com.linkmoa.source.domain.site.dto.request.SiteCreateRequestDto;
import com.linkmoa.source.domain.site.dto.request.SiteDeleteRequestDto;
import com.linkmoa.source.domain.site.dto.request.SiteUpdateRequestDto;
import com.linkmoa.source.domain.site.dto.response.ApiSiteResponse;
import com.linkmoa.source.domain.site.dto.response.SiteGetResponseDto;
import com.linkmoa.source.domain.site.service.SiteService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sites")
public class SiteApiController implements SiteApiSpecification {

    private final SiteService siteService;

    public ResponseEntity<ApiSiteResponse<Long>> saveSite(
            SiteCreateRequestDto siteCreateRequestDto,
            PrincipalDetails principalDetails)
    {
        ApiSiteResponse<Long> siteCreateResponse = siteService.createSite(siteCreateRequestDto, principalDetails);

        return ResponseEntity.ok().body(siteCreateResponse);
    }

    public ResponseEntity<ApiSiteResponse<Long>> updateSite(
            SiteUpdateRequestDto siteCreateRequestDto,
            PrincipalDetails principalDetails)
    {
        ApiSiteResponse<Long> siteUpdateResponse = siteService.updateSite(siteCreateRequestDto, principalDetails);
        return ResponseEntity.ok().body(siteUpdateResponse);
    }

    public ResponseEntity<ApiSiteResponse<Long>> deleteSite(
            SiteDeleteRequestDto siteDeleteRequestDto,
            PrincipalDetails principalDetails) {
        ApiSiteResponse<Long> siteDeleteResponse = siteService.deleteSite(siteDeleteRequestDto, principalDetails);

        return ResponseEntity.ok().body(siteDeleteResponse);
    }



/*

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiSiteResponse<Long>> saveSite(
            @RequestBody @Validated SiteCreateRequestDto siteCreateRequestDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails
            )
    {
        ApiSiteResponse<Long> apiSiteResponse = siteService.saveSite(siteCreateRequestDto,principalDetails);

        return ResponseEntity.ok().body(apiSiteResponse);
    }


    @PutMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiSiteResponse<SiteGetResponseDto>> updateSite(
            @RequestBody @Validated SiteUpdateRequestDto siteUpdateRequestDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ){

        ApiSiteResponse<SiteGetResponseDto> apiSiteUpdateResponse = siteService.updateSite(siteUpdateRequestDto, principalDetails);

        return ResponseEntity.ok().body(apiSiteUpdateResponse);
    }

    @DeleteMapping("/{siteId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiSiteResponse<Long>> deleteSite(
            @PathVariable("siteId") @NotBlank Long siteId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ){
        ApiSiteResponse<Long> apiSiteResponse =siteService.deleteSite(siteId,principalDetails);

        return ResponseEntity.ok().body(apiSiteResponse);
    }

    @GetMapping("/{siteId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiSiteResponse<SiteGetResponseDto>> getSite(
            @PathVariable("siteId") @NotBlank Long siteId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ){
        ApiSiteResponse<SiteGetResponseDto> siteGetResponseDtoApiSiteResponse = siteService.getSite(siteId, principalDetails);

        return ResponseEntity.ok().body(siteGetResponseDtoApiSiteResponse);
    }
*/


/*    @GetMapping("/{directoryId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiSiteResponse<List<SiteGetResponseDto>>> getSiteList(
            @PathVariable @NotBlank Long directoryId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ){

        ApiSiteResponse<List<SiteGetResponseDto>> listApiSiteResponse = siteService.getSiteList(directoryId, principalDetails);

        return ResponseEntity.ok().body(listApiSiteResponse);
    }*/


}

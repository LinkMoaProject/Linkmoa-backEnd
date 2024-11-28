package com.linkmoa.source.domain.site.service;


import com.google.protobuf.Api;
import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.directory.entity.Directory;
import com.linkmoa.source.domain.directory.error.DirectoryErrorCode;
import com.linkmoa.source.domain.directory.exception.DirectoryException;
import com.linkmoa.source.domain.directory.repository.DirectoryRepository;
import com.linkmoa.source.domain.site.dto.request.*;
import com.linkmoa.source.domain.site.dto.response.ApiSiteResponse;
import com.linkmoa.source.domain.site.dto.response.SiteGetResponseDto;
import com.linkmoa.source.domain.site.entity.Site;
import com.linkmoa.source.domain.site.error.SiteErrorCode;
import com.linkmoa.source.domain.site.exception.SiteException;
import com.linkmoa.source.domain.site.repository.SiteRepository;
import com.linkmoa.source.global.aop.annotation.ValidationApplied;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class SiteService {

    private final SiteRepository siteRepository;
    private final DirectoryRepository directoryRepository;

    @ValidationApplied
    public ApiSiteResponse<Long> createSite(SiteCreateRequestDto siteCreateRequestDto, PrincipalDetails principalDetails) {

        Directory directory = directoryRepository.findById(siteCreateRequestDto.directoryId())
                .orElseThrow(() -> new DirectoryException(DirectoryErrorCode.DIRECTORY_NOT_FOUND));

        Site newSite = Site.builder()
                .siteName(siteCreateRequestDto.siteName())
                .siteUrl(siteCreateRequestDto.siteUrl())
                .directory(directory)
                .build();

        siteRepository.save(newSite);

        return ApiSiteResponse.<Long>builder()
                .httpStatusCode(HttpStatus.OK)
                .successMessage("site 생성에 성공했습니다.")
                .data(newSite.getId())
                .build();
    }

    @ValidationApplied
    public ApiSiteResponse<Long> updateSite(SiteUpdateRequestDto siteUpdateRequestDto, PrincipalDetails principalDetails) {

        Site updateSite = siteRepository.findById(siteUpdateRequestDto.siteId())
                .orElseThrow(() -> new SiteException(SiteErrorCode.SITE_NOT_FOUND));

        updateSite.updateSiteNameAndUrl(siteUpdateRequestDto.siteName(), siteUpdateRequestDto.siteUrl());

        return ApiSiteResponse.<Long>builder()
                .httpStatusCode(HttpStatus.OK)
                .successMessage("site 수정(이름,url)에 성공했습니다.")
                .data(updateSite.getId())
                .build();

    }

    @ValidationApplied
    public ApiSiteResponse<Long> deleteSite(SiteDeleteRequestDto siteDeleteRequestDto, PrincipalDetails principalDetails) {

        Site deleteSite = siteRepository.findById(siteDeleteRequestDto.siteId())
                .orElseThrow(() -> new SiteException(SiteErrorCode.SITE_NOT_FOUND));

        siteRepository.delete(deleteSite);


        return ApiSiteResponse.<Long>builder()
                .httpStatusCode(HttpStatus.OK)
                .successMessage("site 삭제에 성공했습니다.")
                .data(deleteSite.getId())
                .build();
    }

    @ValidationApplied
    public ApiSiteResponse<Long> moveSite(SiteMoveRequestDto siteMoveRequestDto, PrincipalDetails principalDetails) {

        Site moveSite = siteRepository.findById(siteMoveRequestDto.siteId())
                .orElseThrow(() -> new SiteException(SiteErrorCode.SITE_NOT_FOUND));

        Directory targetDirectory = directoryRepository.findById(siteMoveRequestDto.targetDirectoryId())
                .orElseThrow(() -> new DirectoryException(DirectoryErrorCode.DIRECTORY_NOT_FOUND));


        moveSite.setDirectory(targetDirectory);

        return ApiSiteResponse.<Long>builder()
                .httpStatusCode(HttpStatus.OK)
                .successMessage("site 위치 이동에 성공했습니다.")
                .data(moveSite.getId())
                .build();


    }


/*    @Transactional
    @ValidationApplied
    public ApiSiteResponse<Long> deleteSite(SiteDeleteRequestDto requestDto)*/

    /*


    @Transactional
    public ApiSiteResponse<Long> deleteSite(Long siteId, PrincipalDetails principalDetails){

        Site deletedSite = siteRepository.findById(siteId)
                .orElseThrow(()->new SiteException(SiteErrorCode.SITE_NOT_FOUND));


        //공통 관심사 부분

        siteRepository.delete(deletedSite);

        return ApiSiteResponse.<Long>builder()
                .httpStatusCode(HttpStatus.OK)
                .successMessage("site 삭제에 성공했습니다.")
                .data(deletedSite.getId())
                .build();

    }

    @Transactional
    public ApiSiteResponse<SiteGetResponseDto> updateSite(SiteUpdateRequestDto siteUpdateRequestDto,PrincipalDetails principalDetails){
      Site updatedSite = siteRepository.findById(siteUpdateRequestDto.siteId())
              .orElseThrow(()->new SiteException(SiteErrorCode.SITE_NOT_FOUND));


      //공통 관심사 부분


      updatedSite.updateSite(siteUpdateRequestDto.siteName(),siteUpdateRequestDto.siteUrl());

      SiteGetResponseDto siteGetResponseDto = SiteGetResponseDto.builder()
              .siteName(siteUpdateRequestDto.siteName())
              .siteUrl(siteUpdateRequestDto.siteUrl())
              .build();


      return ApiSiteResponse.<SiteGetResponseDto>builder()
              .httpStatusCode(HttpStatus.OK)
              .successMessage("site 수정에 성공했습니다.")
              .data(siteGetResponseDto)
              .build();

    }

    @Transactional
    public ApiSiteResponse<SiteGetResponseDto> getSite(Long siteId,PrincipalDetails principalDetails){
        Site getSite = siteRepository.findById(siteId)
                .orElseThrow(()->new SiteException(SiteErrorCode.SITE_NOT_FOUND));


        //공통 관심사 부분

        SiteGetResponseDto siteGetResponse = SiteGetResponseDto.builder()
                .siteName(getSite.getSiteName())
                .siteUrl(getSite.getSiteUrl())
                .build();


        return ApiSiteResponse.<SiteGetResponseDto>builder()
                .httpStatusCode(HttpStatus.OK)
                .successMessage("단일 site 조회에 성공했습니다.")
                .data(siteGetResponse)
                .build();

    }

    @Transactional
    public ApiSiteResponse<List<SiteGetResponseDto>> getSiteList(Long directoryId,PrincipalDetails principalDetails){

        // 공통 관심사 부분


        Directory directory = directoryRepository.findById(directoryId)
                .orElseThrow(()->new DirectoryException(DirectoryErrorCode.DIRECTORY_NOT_FOUND));

        List<Site> sites = directory.getSites();

        List<SiteGetResponseDto> siteGetResponseDtoList = sites.stream()
                .map(site -> SiteGetResponseDto.builder()
                        .siteName(site.getSiteName())
                        .siteUrl(site.getSiteUrl())
                        .build())
                .collect(Collectors.toList());

        return ApiSiteResponse.<List<SiteGetResponseDto>>builder()
                .httpStatusCode(HttpStatus.OK)
                .successMessage("Directory 내 site 목록 조회에 성공했습니다.")
                .build();
    }
*/







}

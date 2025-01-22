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
import java.util.stream.Stream;

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


        Integer nextOrderIndex = directory.getNextOrderIndex();


        Site newSite = Site.builder()
                .siteName(siteCreateRequestDto.siteName())
                .siteUrl(siteCreateRequestDto.siteUrl())
                .directory(directory)
                .orderIndex(nextOrderIndex)
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

        Integer newOrderIndex = targetDirectory.getNextOrderIndex();
        moveSite.setDirectory(targetDirectory);

        moveSite.setOrderIndex(newOrderIndex);

        return ApiSiteResponse.<Long>builder()
                .httpStatusCode(HttpStatus.OK)
                .successMessage("site의 위치를 다른 directory로 위치 이동에 성공했습니다.")
                .data(moveSite.getId())
                .build();


    }



}

package com.linkmoa.source.domain.site.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.directory.entity.Directory;
import com.linkmoa.source.domain.directory.error.DirectoryErrorCode;
import com.linkmoa.source.domain.directory.exception.DirectoryException;
import com.linkmoa.source.domain.directory.repository.DirectoryRepository;
import com.linkmoa.source.domain.site.dto.request.SiteCreateRequestDto;
import com.linkmoa.source.domain.site.dto.request.SiteDeleteRequestDto;
import com.linkmoa.source.domain.site.dto.request.SiteMoveRequestDto;
import com.linkmoa.source.domain.site.dto.request.SiteUpdateRequestDto;
import com.linkmoa.source.domain.site.entity.Site;
import com.linkmoa.source.domain.site.error.SiteErrorCode;
import com.linkmoa.source.domain.site.exception.SiteException;
import com.linkmoa.source.domain.site.repository.SiteRepository;
import com.linkmoa.source.global.aop.annotation.ValidationApplied;
import com.linkmoa.source.global.spec.ApiResponseSpec;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class SiteService {

	private final SiteRepository siteRepository;
	private final DirectoryRepository directoryRepository;

	@ValidationApplied
	public ApiResponseSpec<Long> createSite(SiteCreateRequestDto siteCreateRequestDto,
		PrincipalDetails principalDetails) {

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
		return ApiResponseSpec.success(
			HttpStatus.OK,
			"site 생성에 성공했습니다.",
			newSite.getId()
		);

	}

	@ValidationApplied
	public ApiResponseSpec<Long> updateSite(SiteUpdateRequestDto siteUpdateRequestDto,
		PrincipalDetails principalDetails) {

		Site updateSite = siteRepository.findById(siteUpdateRequestDto.siteId())
			.orElseThrow(() -> new SiteException(SiteErrorCode.SITE_NOT_FOUND));

		updateSite.updateSiteNameAndUrl(siteUpdateRequestDto.siteName(), siteUpdateRequestDto.siteUrl());

		return ApiResponseSpec.success(
			HttpStatus.OK,
			"site 수정(이름,url)에 성공했습니다.",
			updateSite.getId()
		);

	}

	@ValidationApplied
	public ApiResponseSpec<Long> deleteSite(SiteDeleteRequestDto siteDeleteRequestDto,
		PrincipalDetails principalDetails) {

		Site deleteSite = siteRepository.findById(siteDeleteRequestDto.siteId())
			.orElseThrow(() -> new SiteException(SiteErrorCode.SITE_NOT_FOUND));

		Directory parentDirectory = deleteSite.getDirectory();
		Integer orderIndex = deleteSite.getOrderIndex();

		directoryRepository.decrementDirectoryAndSiteOrderIndexes(parentDirectory, orderIndex);
		siteRepository.delete(deleteSite);

		return ApiResponseSpec.success(
			HttpStatus.OK,
			"site 삭제에 성공했습니다.",
			deleteSite.getId()
		);
	}

	@ValidationApplied
	public ApiResponseSpec<Long> moveSite(SiteMoveRequestDto siteMoveRequestDto, PrincipalDetails principalDetails) {

		Site moveSite = siteRepository.findById(siteMoveRequestDto.siteId())
			.orElseThrow(() -> new SiteException(SiteErrorCode.SITE_NOT_FOUND));

		Directory targetDirectory = directoryRepository.findById(siteMoveRequestDto.targetDirectoryId())
			.orElseThrow(() -> new DirectoryException(DirectoryErrorCode.DIRECTORY_NOT_FOUND));

		directoryRepository.decrementDirectoryAndSiteOrderIndexes(moveSite.getDirectory(), moveSite.getOrderIndex());

		Integer newOrderIndex = targetDirectory.getNextOrderIndex();

		moveSite.setDirectory(targetDirectory);

		moveSite.setOrderIndex(newOrderIndex);

		return ApiResponseSpec.success(
			HttpStatus.OK,
			"site의 위치를 다른 directory로 위치 이동에 성공했습니다.",
			moveSite.getId()
		);

	}

}

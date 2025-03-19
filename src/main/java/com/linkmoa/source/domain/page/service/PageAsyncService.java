package com.linkmoa.source.domain.page.service;

import com.linkmoa.source.domain.directory.dto.response.DirectoryDetailResponse;
import com.linkmoa.source.domain.directory.dto.response.DirectoryResponse;
import com.linkmoa.source.domain.directory.repository.DirectoryRepository;
import com.linkmoa.source.domain.page.dto.response.PageDetailsResponse;
import com.linkmoa.source.domain.page.entity.Page;
import com.linkmoa.source.domain.site.dto.response.SiteDetailResponse;
import com.linkmoa.source.domain.site.repository.SiteRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class PageAsyncService {

	private final DirectoryRepository directoryRepository;
	private final SiteRepository siteRepository;

	@Async("threadPoolTaskExecutor")
	public CompletableFuture<List<DirectoryDetailResponse>> findDirectoryDetailsAsync(Long directoryId,
		Set<Long> favoriteDirectoryIds) {
		return CompletableFuture.completedFuture(
			directoryRepository.findDirectoryDetails(directoryId, favoriteDirectoryIds));
	}

	@Async("threadPoolTaskExecutor")
	public CompletableFuture<List<SiteDetailResponse>> findSitesDetailsAsync(Long directoryId,
		Set<Long> favoriteSiteIds) {
		return CompletableFuture.completedFuture(siteRepository.findSitesDetails(directoryId, favoriteSiteIds));
	}

	public CompletableFuture<PageDetailsResponse> combinePageDetails(
		CompletableFuture<List<DirectoryDetailResponse>> directoryDetailsFuture,
		CompletableFuture<List<SiteDetailResponse>> sitesDetailsFuture,
		Page page
	) {
		return directoryDetailsFuture.thenCombine(sitesDetailsFuture, (directoryDetails, sitesDetails) -> {
			// 두 비동기 결과를 결합하여 페이지 세부 정보를 준비
			PageDetailsResponse pageDetailsResponse = PageDetailsResponse.builder()
				.pageId(page.getId())
				.pageTitle(page.getPageTitle())
				.pageDescription(page.getPageDescription())
				.directoryDetailRespons(directoryDetails)
				.siteDetailResponses(sitesDetails)
				.build();
			return pageDetailsResponse;
		});
	}

}
